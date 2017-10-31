package com.bjsxt.height.Completable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Stream;
class whenCompate2{
    private static Random rand = new Random();
    private static long t = System.currentTimeMillis();
    static int getMoreData() {
        System.out.println("begin to start compute");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t)/1000 + " seconds");
        return rand.nextInt(1000);
    }
    public static void main(String[] args) throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(whenCompate2::getMoreData);
        Future<Integer> f = future.whenComplete((v, e) -> {
            System.out.println(v);
            System.out.println(e);
        });
        System.out.println(f.get());
        System.in.read();
    }
}
class newCachedThreadPool{
	public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionStage<Double> futurePrice = CompletableFuture.supplyAsync(() -> {
        	//CompletableFuture 根据任务的主从关系为:
            try {//提交任务的方法，如静态方法 supplyAsync(supplier[, executor]),  runAsync(runnable[, executor])
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 23.55;
        }, executor);
 
        System.out.println(111);
        futurePrice.thenAccept(System.out::println);
		// 回调函数，即对任务执行后所作出回应的方法，多数方法了，如 thenRun(action), thenRunAsync(action[,
		// executor]), whenComplete(action), whenCompleteAsync(action[, executor]) 等
        System.out.println(222);
//        根据执行方法可分为同步与异步方法，任务都是要被异步执行，所以提交任务的方法都是异步的。而对任务作出回应的方法很多分为两个版本，如
//
//        同步方法，如 thenRun(action), whenComplete(action)
//        异步方法，如 thenRunAsync(action[, executor]), whenCompleteAsync(action[, executor]), 异步方法可以传入线程池，否则用默认的
//        因此所要理解的 CompletableFuture 的线程会涉及到任务与回调函数所使用的线程。
        executor.shutdown();
//      111
//      222
//      23.55
        //executor.shutdown() 并不是立即关掉线程池，而是采取更温柔, 安全的方式，等线程池中没有正在执行的任务时才关闭，从而结束主程序。
        

//        public CompletableFuture<T> 	whenComplete(BiConsumer<? super T,? super Throwable> action)
//        public CompletableFuture<T> 	whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
//        public CompletableFuture<T> 	whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
//        public CompletableFuture<T>     exceptionally(Function<Throwable,? extends T> fn)
        //可以看到Action的类型是BiConsumer<? super T,? super Throwable>，它可以处理正常的计算结果，或者异常情况。      ？？？？？？？？？？？？？？？？？？？
        	//方法不以Async结尾，意味着Action使用相同的线程执行，而Async可能会使用其它的线程去执行(如果使用相同的线程池，也可能会被同一个线程选中执行)。
    }
}
/**
 * @author pc_udbnnh
 *
 */
class supplyAsyncExecutorService{
    public static void main(String[] args) throws IOException {
        CompletionStage<Double> futurePrice = CompletableFuture.supplyAsync(() -> {//Job
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 23.55;
        });
        System.out.println(111);
        futurePrice.thenAccept(System.out::println);
        System.out.println(222);
 
        System.in.read();
//      111
//      222
//      23.55
		// 同样的输出结果。但如果把上面的 System.in.read() 移除掉，将看不到 23.55 的输出程序就直接退出了，为什么了呢？因为
		// CompletableFuture.supplyAsync() 方法默认把任务提交到 ForkJoinPool 线程池中执行，而它的线程设置了
		// daemon 属性为 true, 所以它阻止不了主线程的退出，才用 System.in.read()
		// 维持主线程的执行。如果换成别的线程池类型就可不需要代码 System.in.read(), 再变
    }
}
/**我们多用 supplyAsync(...) 静态方法来获得 CompletableFuture 实例
 * @author pc_udbnnh
 *
 */
class supplyAsyncTest{
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/* CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
	        try {
	            if(true) {
	                throw new RuntimeException("");
	            }
	            futurePrice.complete(23.5);
	        } catch (Exception ex) {
	            futurePrice.completeExceptionally(ex); //捕获的异常还会由 ExecutionException 包裹一下
	        }
        }).start();
     
        System.out.println(futurePrice.get());
        //CompletableFuture 的异常处理

        //如果在设置 CompletableFuture.complete(value) 之前出现了异常，那么 get() 或其他回调函数像 whenComplete() 都会无限期的等待下去
        //办法一是调用 get(timeout) 时给定一个超时时间，如果指定时间内还没有获得结果则得到 TimeoutException。
        //另一种办法是要在线程中通过 completeExceptionally(ex) 来传播异常
       futurePrice.whenComplete((aDouble,throwable) -> {
    	   System.out.println(aDouble);
        });*/
        
		
       CompletableFuture<Double> futurePrice2 = CompletableFuture.supplyAsync(() -> {
           /*if (true) {
               throw new RuntimeException("Something wrong");
           }*/
           return 23.5;
       }, runnable -> new Thread(runnable).start());//使用start启动新的线程
    
       System.out.println(futurePrice2.get());
	}
}
/**执行回调函数，不阻塞后续操作
 * @author pc_udbnnh
 *
 */
class whenComplete{
	public static void main(String[] args) {
		CompletableFuture<Double> futurePrice = getPriceAsync();
		  
        //do anything you want, 当前线程不被阻塞
        System.out.println(111);
 
        //线程任务完成的话，执行回调函数，不阻塞后续操作
        futurePrice.whenComplete((aDouble, throwable) -> {
            System.out.println(aDouble);
            //do something else
        });
 
        System.out.println(222);
	}
	static CompletableFuture<Double> getPriceAsync() {
	    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
	    new Thread(() -> {
	        try {
	            Thread.sleep(5000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        futurePrice.complete(23.55);
	    }).start();
	    return futurePrice;
	}
}

public class FunctionImpl {
	
	 void foo(Function f) { f.call("a"); }


public Integer findFirstMatch() {
  return Stream.of(1, 4, 2, 5, 6, 3)
    .filter(i -> i > 3)
    .findFirst()
    .orElse(null);
}

public static Integer findFirstMatch1() {
	  return Stream.of( 6,1, 4, 2, 5, 3)
	    .filter(i -> {
	      System.out.println("filter: " + i);
	      return i > 3;
	    })
	    .findFirst()
	    .orElse(null);
	}
	 
	//

public static Integer findFirstMatch2() {
return (int) Stream.of(1, 4, 2, 5, 6, 3)
  .filter(z -> {
    System.out.println("filter: " + z);
    return z > 3;
  }).limit(2).count();
}

//========================================>表达式异常处理
public void foo2() {
    Stream.of("a", "b").forEach(new Consumer<String>() {
        @Override
        public void accept(String s) {
            try {
				new FileInputStream(s).close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
});
}

public static void foo1() {
    Stream.of("a", "b").forEach(s-> {
		try {	
			
			
			    new Thread(() -> {
			        throw new RuntimeException("Something wrong");
			    }).start();
			    //这里怎么延时也没用
			    new FileInputStream(s).close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage()); //上面的异常永远也不关这里的事
				throw new RuntimeException("file not found");
			}
});
}



private static void foo3() {
    Arrays.asList("a", "b").parallelStream().forEach(s -> {
        try {
            new FileInputStream(s).close();
        } catch (IOException e) {
            throw new RuntimeException("file not found");
        }
    });
}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
	       
	       
	   /* try {
	        foo3();
	    } catch (Exception ex) {
	        System.out.println("Caught exception: " + ex.getMessage());
	        ex.printStackTrace();
	    }
		*/
		//foo1();
		//FunctionImpl fi = new FunctionImpl();
		//. 参数类型总是可省略, 基本上我们总是省略掉参数类型
		/*fi.foo((x, y) -> { // (x, y) 完整写法是 (String x, String y)
			System.out.println(x);
			System.out.println(y);
		});
		*/
		//. 参数为一个时，参数括号省略
		/*Function f = x -> {
			  System.out.println();
			  System.out.println(x);
			};
			f.call("Hello");
			*/
			
			//System.out.println(fi.findFirstMatch());
			/*System.out.println(findFirstMatch1());
			System.out.println("===================");
			Stream.of(1, 4, 2, 5, 6, 3)
			  .filter(z -> {
			    System.out.println("filter: " + z);
			    return z > 3;
			  }).limit(2).count();*/
			//. 语句为一条时，可省略大括号, 并且语句后不要分号
//			interface Function {
//				  void call(String x);
//				}
//				 
//				void foo(Function f) {
//				  f.call("Hi");
//				}
//				 
//				foo(x -> System.out.println(x)); //不能写成 foo(x -> System.out.println(x);); 这像话	
//			
			//. 如果是单条 return 语句，省去大括时必须把 return 关键字去掉

//interface Function {
//  void String call(String x);
//}
// 
//foo(x -> "Hello " + x)
			
			//. 参数部分的括号总是需要
		/*	interface Function {
				  void call();
				}
				 
				Function f = () -> System.out.println();*/
	}
}
