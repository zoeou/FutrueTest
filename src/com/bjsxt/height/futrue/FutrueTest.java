package com.bjsxt.height.futrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutrueTest {
	public static void main(String[] args) {

		ExecutorService executor = Executors.newCachedThreadPool(); // 这是众多线程池类型的一种
		Future<String> future = executor.submit(() -> { // Lambda 是一个 callable， 提交后便立即执行，这里返回的是 FutureTask 实例
			System.out.println("Running task...");
			Thread.sleep(5000);
			return "Task return";
		});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		System.out.println("Do something else"); // 前面的的 Callable 在其他线程中运行着，现在想做别的事情都不影响

		try {
			System.out.println(future.get()); // 等待 future 的执行结果
		} catch (InterruptedException | ExecutionException e) {
		}

		executor.shutdown(); // 不关闭的话程序都不会退出
	}
}
