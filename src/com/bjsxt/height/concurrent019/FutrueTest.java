package com.bjsxt.height.concurrent019;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutrueTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
/*        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<Integer> f = es.submit(() ->{
                // 长时间的异步计算
                // ……
                // 然后返回结果
        	System.out.println("futureTest....");

                return 100;
            });
        while(!f.isDone())
            ;
        f.get();*/
    	
    	
    	CompletableFuture<Integer> future = CompletableFuture.supplyAsync(
    			() -> {//CompletableFuture.supplyAsync() : 异步获取方法返回值
    	    //int i = 1/0;
    	    return 100;
    	});
    	//future.join();//join返回计算的结果或者抛出一个unchecked异常(CompletionException)，
    	future.get();//与get对抛出的异常的处理有些细微的区别

    }
}

/*Exception in thread "main" java.util.concurrent.ExecutionException: java.lang.ArithmeticException: / by zero
at java.util.concurrent.CompletableFuture.reportGet(Unknown Source)
at java.util.concurrent.CompletableFuture.get(Unknown Source)
at com.bjsxt.height.concurrent019.FutrueTest.main(FutrueTest.java:30)
Caused by: java.lang.ArithmeticException: / by zero
at com.bjsxt.height.concurrent019.FutrueTest.lambda$0(FutrueTest.java:26)
at java.util.concurrent.CompletableFuture$AsyncSupply.run(Unknown Source)
at java.util.concurrent.CompletableFuture$AsyncSupply.exec(Unknown Source)
at java.util.concurrent.ForkJoinTask.doExec(Unknown Source)
at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(Unknown Source)
at java.util.concurrent.ForkJoinPool.runWorker(Unknown Source)
at java.util.concurrent.ForkJoinWorkerThread.run(Unknown Source)*/


/*join异常：
Exception in thread "main" java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
at java.util.concurrent.CompletableFuture.encodeThrowable(Unknown Source)
at java.util.concurrent.CompletableFuture.completeThrowable(Unknown Source)
at java.util.concurrent.CompletableFuture$AsyncSupply.run(Unknown Source)
at java.util.concurrent.CompletableFuture$AsyncSupply.exec(Unknown Source)
at java.util.concurrent.ForkJoinTask.doExec(Unknown Source)
at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(Unknown Source)
at java.util.concurrent.ForkJoinPool.runWorker(Unknown Source)
at java.util.concurrent.ForkJoinWorkerThread.run(Unknown Source)
Caused by: java.lang.ArithmeticException: / by zero
at com.bjsxt.height.concurrent019.FutrueTest.lambda$0(FutrueTest.java:26)
... 6 more
*/