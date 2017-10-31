package com.bjsxt.height.Completable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
	public static void main(String[] args) {
	    try {
	        foo3();
	    } catch (Exception ex) {
	        System.out.println("Caught exception: " + ex.getMessage());
	        ex.printStackTrace();
	    }
		
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
