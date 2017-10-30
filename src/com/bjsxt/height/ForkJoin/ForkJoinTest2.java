package com.bjsxt.height.ForkJoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

@SuppressWarnings("serial")
public class ForkJoinTest2 extends RecursiveTask<List<String>> {
	private int threshold;// 阈值
	private List<String> li;// 待拆分list

	public ForkJoinTest2(int threshold, List<String> li) {  
	        this.threshold = threshold;  
	        this.li = li;  
	    }

	@Override
	protected List<String> compute() {

		List<String> newList = new ArrayList<String>();// 保存含有a字母的新集合
		System.out.println("当前线程===>"+Thread.currentThread().getName());
		// 当end与start之间的差小于阈值时，开始进行实际筛选
		if (li.size() < threshold) {
			for (String s : li) {
				if (s.contains("a")) {// 如果含有字母a，则添加进去
					newList.add(s);
				} else {
					continue;
				}
			}
		} else {
			// 如果当end与start之间的差大于阈值时,将大任务分解成两个小任务。
			int middles = li.size() / 2;

			List<String> leftli = li.subList(0, middles);

			List<String> rightli = li.subList(middles, li.size());

			ForkJoinTest2 left = new ForkJoinTest2(threshold, leftli);

			ForkJoinTest2 right = new ForkJoinTest2(threshold, rightli);

			// 并行执行两个小任务
			left.fork();
			right.fork();

			// 把两个小任务的结果“集合”起来
			newList.addAll(left.join());
			newList.addAll(right.join());
		}

		return newList;
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		String[] strings = { "a", "ah", "b", "ba", "ab", "ac", "sd", "fd", "ar", "te", "se", "te", "sdr", "gdf", "df",
				"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui", "al" , "a", "ah", "b", "ba", "ab", "ac", "sd", "fd", "ar", "te", "se", "te", "sdr", "gdf", "df",
				"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui", "al" , "a", "ah", "b", "ba", "ab", "ac", "sd", "fd", "ar", "te", "se", "te", "sdr", "gdf", "df",
				"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui", "al" , "a", "ah", "b", "ba", "ab", "ac", "sd", "fd", "ar", "te", "se", "te", "sdr", "gdf", "df",
				"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui", "al" , "a", "ah", "b", "ba", "ab", "ac", "sd", "fd", "ar", "te", "se", "te", "sdr", "gdf", "df",
				"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui", "al" ,"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui","fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui"
				,"fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui","fg", "gh", "oa", "ah", "qwe", "re", "ty", "ui"};

		List<String> stringList = new ArrayList<String>(Arrays.asList(strings));// 把数组转换成list集合

		ForkJoinPool forkJoinPool = new ForkJoinPool();// 创建他的线程池

		// 提交可分解的ForkJoinTask任务
		Future<List<String>> ft = forkJoinPool.submit(new ForkJoinTest2(20, stringList));

		System.out.println("查询的结果是：" + ft.get() + ",个数是：" + ft.get().size());

		forkJoinPool.shutdown();// 关闭线程池
	}
}

