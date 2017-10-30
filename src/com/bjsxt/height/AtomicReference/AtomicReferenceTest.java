package com.bjsxt.height.AtomicReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
	public static void main(String[] args) {
		AtomicReference<List<Object>> reference = new AtomicReference<>();
		 
		new Thread(() -> {
			List<Object> list = new ArrayList<>();
			list.add(new Object());
			list.add(2);
			try {
				Thread.sleep(2000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  //do something that is time-consuming
		  reference.set(list);  //任务完成完设置 reference 的值
		}).start();
		 
		while(reference.get() == null) { //耐心的等待，直到 reference.get() 有值为止
		}
		 
		System.out.println("Finally, " + reference.get());//回调结果
	}
}
