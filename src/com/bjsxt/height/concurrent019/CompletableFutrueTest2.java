package com.bjsxt.height.concurrent019;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutrueTest2 {


public static CompletableFuture<Integer> compute() {
    final CompletableFuture<Integer> future = new CompletableFuture<>();
    return future;
}
public static void main(String[] args) throws Exception {
    final CompletableFuture<Integer> f = compute();//初始化
    class Client extends Thread {//内部类，执行线程
        CompletableFuture<Integer> f;//使用外部对象
        Client(String threadName, CompletableFuture<Integer> f) {
            super(threadName);
            this.f = f;
        }
        @Override
        public void run() {
            try {
                System.out.println(this.getName() + ": " + f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    new Client("Client1", f).start();
    new Client("Client2", f).start();
    System.out.println("waiting");
    f.complete(100);
    //f.completeExceptionally(new Exception());
    System.in.read();
}
}

//CompletableFuture.completedFuture是一个静态辅助方法，用来返回一个已经计算好的CompletableFuture