package com.bjsxt.height.Completable;


import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by ibm on 2017/5/3.
 */
public class CompletableTest3 {

    public static void main(String[] args) {
        long st = System.nanoTime();//1638390918463
        CompletableTest3 shopExercise = new CompletableTest3();
        shopExercise.getPrices("xiaozhen");
        long et = System.nanoTime();
        System.out.println("耗时： " + (et -st)/1000_1000 + "ms");
    }

    /**
     * 配置一个线程池的执行器，其中线程的数量等于查询的个数，然后设置为守护线程（不会阻止程序退出）
     * 为什么使用线程池：因为开启的线程过多，返回会造成查询缓慢
     */
    final Executor executor = Executors.newFixedThreadPool(10, (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    /**
     * 查询某个商品在每个商店的价格
     */
    public List<Double> getPrices(String product){
        //初始化商店列表
        List<String> shops = Arrays.asList("A_SHOP","B_SHOP","C_SHOP","D_SHOP","E_SHOP");
        //并行查询 + 异步请求
        List<CompletableFuture<Double>> futures = shops
                .parallelStream()
                .map(s -> CompletableFuture.supplyAsync(() -> getPrice(product,s),executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                    CompletableFuture.supplyAsync(() -> DiscountService.apply(quote),executor)
                ))
                .collect(Collectors.toList());
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        //=====下面的方式：在同一个stream中进行两次map操作：查询会顺序执行，因为流水线操作是顺序执行同步的====
        //return shops.parallelStream().map(s -> CompletableFuture.supplyAsync(() -> {
        //    StringBuilder result = new StringBuilder(s);
        //    result.append(" : " + getPrice(product));
        //    return result.toString();
        //}))
        //.map(CompletableFuture::join)
        //.collect(Collectors.toList());
    }
    /**
     * 同步获取价格
     */
    public String getPrice(String product,String shopName){
        double price = calculatePrice(product);
        Discount discount = Discount.CLOD;
        String result = String.format("\n%s:%s:%s:%s:%s",shopName,product,price + "",discount.value + "",discount.desc);
        System.out.print(result);
        return result;
    }
    /**
     * 计算价格：耗时查询 + 随机价格
     * @param product 商品名称
     * @return 随机价格
     */
    private double calculatePrice(String product){
        delay();
        return RandomUtils.nextDouble(10,100);
    }
    /**
     * 制造耗时查询
     */
    private void delay(){
        try {
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 折扣
     */
    enum Discount{

        NONE(1,"没优惠"),
        SILVER(0.7f,"七折"),
        CLOD(0.5f,"五折"),
        DIAMOND(0.3f,"三折");

        double value;
        String desc;

         Discount(double value,String desc){
            this.value = value;
            this.desc = desc;
        }
    }
//=============================================================
    /**
     * 异步获取价格
     */
    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        //在另一个线程中计算价格，当计算完成过后，将price赋值给future对象
        new Thread(() -> {
            try{
                double price = calculatePrice(product);
                futurePrice.complete(price);
            }catch (Exception e){
                //如果有异常，抛出去避免主线永久程阻塞
                futurePrice.completeExceptionally(e);
            }
        }).start();
        //使用supplyAsync工厂方法,两种方式完全等效
        //CompletableFuture<Double> futurePrice2 = CompletableFuture.supplyAsync(() -> calculatePrice(product));
        return futurePrice;
    }
}
