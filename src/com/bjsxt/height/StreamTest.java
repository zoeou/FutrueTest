package com.bjsxt.height;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
	public static void main(String[] args) {
		// 静态工厂方法
		Stream<String> stringStream = Stream.of("A");// of方法，其生成的Stream是有限长度的，Stream的长度为其内的元素个数。
		System.out.println(stringStream);

		Stream<Double> generateA = Stream.generate(new Supplier<Double>() {
			@Override
			public Double get() {
				return java.lang.Math.random();
			}
		});
		Stream<Double> generateB = Stream.generate(() -> java.lang.Math.random());
		Stream<Double> generateC = Stream.generate(java.lang.Math::random);
		System.out.println(generateA + "  " + generateB + "   " + generateC);

		// iterate方法，其返回的也是一个无限长度的Stream，与generate方法不同的是，其是通过函数f迭代对给指定的元素种子而
		// 产生无限连续有序Stream，其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环。
		Stream.iterate(5, item -> item + 1).limit(10).forEach(System.out::print);
		// 打印结果：1，2，3，4，5，6，7，8，9，10 种子为1,长度为10,遍历

		// empty方法返回一个空的顺序Stream，该Stream里面不包含元素项

		int ids[] = new int[] { 1, 2, 3, 4 };
		Arrays.stream(ids).forEach(System.out::println);

		// concat方法将两个Stream连接在一起，合成一个Stream,若两个输入的Stream都时排序的，则新Stream也是排序的；若输入的Stream中任何一个是并行的，则新的Stream也是并行的；若关闭新的Stream时，原两个输入的Stream都将执行关闭处理。
		System.out.println("===========================>");
		Stream.concat(Stream.of(1, 2, 3), Stream.of(4, 5)).forEach(integer -> System.out.print(integer + "  "));
		// 打印结果
		// 1 2 3 4 5
		// distinct方法以达到去除掉原Stream中重复的元素，生成的新Stream中没有没有重复的元素
		Stream.of(1, 2, 3, 1, 2, 3).distinct().forEach(System.out::println); // 打印结果：1，2，3

		// filter方法对原Stream按照指定条件过滤，在新建的Stream中，只包含满足条件的元素，将不满足条件的元素过滤掉。
		// filter传入的Lambda表达式必须是Predicate实例，参数可以为任意类型，而其返回值必须是boolean类型。
		System.out.println("===========================>");
		Stream.of(1, 2, 3, 4, 5).filter(item -> item > 3).forEach(System.out::println);// 打印结果：4，5

		/*
		 * map方法将对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。为了提高处理效率，
		 * 官方已封装好了，三种变形：mapToDouble，mapToInt，mapToLong。其实很好理解，如果想将原Stream中的数据类型，
		 * 转换为double,int或者是long是可以调用相对应的方法。
		 */

		// map传入的Lambda表达式必须是Function实例，参数可以为任意类型，而其返回值也是任性类型，javac会根据实际情景自行推断。
		System.out.println("===========================>");
		Stream.of("a", "b", "25").map(item -> item.toUpperCase()).forEach(System.out::println);
		// 打印结果
		// A, B, HELLO
		System.out.println("===========================>");
		Stream.of(1, 2, 3).flatMap(String /* 这里参数是一个包装类 */ -> Stream.of(String * 10)).forEach(System.out::println);
		// 打印结果
		// 10，20，30

		/*
		 * peek方法生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），
		 * 新Stream每个元素被消费的时候都会执行给定的消费函数，并且消费函数优先执行
		 */
		System.out.println("===========================>");

		Stream.of(1, 2, 3, 4, 5).peek(integer -> System.out.println("accept:" + integer)).forEach(System.out::println);
		// 打印结果
		// accept:1
		// 1
		// accept:2
		// 2
		// accept:3
		// 3
		// accept:4
		// 4
		// accept:5
		// 5
		System.out.println("===========================>");
		/*
		 * sorted方法将对原Stream进行排序，返回一个有序列的新Stream。sorterd有两种变体sorted()，sorted(
		 * Comparator)，前者将默认使用Object.equals(Object)进行排序，而后者接受一个自定义排序规则函数(
		 * Comparator)，可按照意愿排序。
		 */
		Stream.of(5, 4, 3, 2, 1).sorted().forEach(System.out::println);
		// 打印结果
		// 1，2，3,4,5

		Stream.of(1, 2, 3, 4, 5).sorted().forEach(System.out::println);
		// 打印结果
		// 5, 4, 3, 2, 1

		long count = Stream.of(1, 2, 3, 4, 5).count();
		System.out.println("count:" + count);// 打印结果：count:5

		Stream.of(5, 4, 3, 2, 1).sorted().forEach(System.out::println);
		// 打印结果
		// 1，2，3,4,5
		/*
		 * 原Stream根据比较器Comparator，进行排序(升序或者是降序)，所谓的最大值就是从新进行排序的，
		 * max就是取重新排序后的最后一个值，而min取排序后的第一个值。
		 */
		// allMatch操作用于判断Stream中的元素是否全部满足指定条件。如果全部满足条件返回true
		boolean allMatch = Stream.of(1, 2, 3, 4).allMatch(integer -> integer > 2);
		System.out.println("allMatch: " + allMatch); // 打印结果：allMatch: true
		// anyMatch操作用于判断Stream中的是否有满足指定条件的元素。如果最少有一个满足条件返回true
		Optional<Integer> any = Stream.of(1, 2, 3, 4).findFirst();
		System.out.println(any);// Optional[1]

		// noneMatch方法将判断Stream中的所有元素是否满足指定的条件，如果所有元素都不满足条件，返回true；否则，返回false.

		m4();

		// 2.1、得到其中不为空的String
		List<String> filterLists = new ArrayList<>();
		filterLists.add("");
		filterLists.add("a");
		filterLists.add("b");
		List afterFilterLists = filterLists.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

		
		//limit 返回 Stream 的前面 n 个元素；skip 则是扔掉前 n 个元素:
			List<String> forEachLists = new ArrayList<>();
			forEachLists.add("a");
			forEachLists.add("b");
			forEachLists.add("c");
			forEachLists.add("d");
			forEachLists.add("e");
			forEachLists.add("f");
			List<String> limitLists = forEachLists.stream().skip(2).limit(3).collect(Collectors.toList());
		//注意skip与limit是有顺序关系的，比如使用skip(2)会跳过集合的前两个，返回的为c、d、e、f,然后调用limit(3)会返回前3个，所以最后返回的c,d,e
			/*List<Car> lowPriceCar;
	        lowPriceCar = garage.stream() .filter(Car::isLowPriceCar)//isLowPriceCar()方法筛选低价车
	                .collect(toList());	*/
			
			//of方法：有两个overload方法，一个接受变长参数，一个接口单一值
			Stream<Integer> integerStream = Stream.of(1, 2, 3, 5);
			Stream<String> string1Stream = Stream.of("taobao");	
			
			//2. generator方法：生成一个无限长度的Stream，其元素的生成是通过给定的Supplier（这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）
		    Stream.generate(new Supplier<Double>() {
		    	@Override
		    	public Double get() {
					return Math.random();
				}
			});
			Stream.generate(() -> Math.random());
		    Stream.generate(Math::random);
			
	}

	/**
	 * 请解释下面代码的输出：（结尾函数找流对象filter，而流对象filter找流对象map 要数据） map: c2 filter: C2
	 * //流对象filter没有"C2"这个元素 map: a1 filter: A1 forEach: A1 map: a2 filter: A2
	 * forEach: A2 map: b0 filter: B0
	 * 
	 * ok，如果我们交换上面代码中map()和filter()函数的调用顺序，先filter()再map()(代码见下面)，可以减少函数执行的次数。
	 */
	public static void m2() {
		Stream<String> of = Stream.of("c2", "a1", "a2", "b0");
		Stream<String> map = of.map(s -> {
			System.out.println("map: " + s);// 副作用
			return s.toUpperCase();
		});
		Stream<String> filter = map.filter(s -> {
			System.out.println("filter: " + s);
			return s.startsWith("A");
		});
		filter.forEach(s -> System.out.println("forEach: " + s));
	}

	public static void m3() {
		String[] toArray = (String[]) (Stream.of("c2", "a1", "a2", "b0").filter(s -> s.startsWith("A"))
				.map(s -> s.toUpperCase()).toArray());
		Stream.of(toArray).forEach(s -> System.out.println("forEach: " + s));
		long count = Stream.of(toArray).count();
	}

	public static void m4() {
		Supplier<Stream<String>> streamSupplier = () -> Stream.of("c2", "a1", "a2", "b0").filter(s -> s.startsWith("A"))
				.map(s -> s.toUpperCase());

		streamSupplier.get().forEach(s -> System.out.println("forEach: " + s));
		streamSupplier.get().count();
	}
}

class Student {

	public static void main(String[] args) {
		Supplier<Stream<String>> streamSupplier = () -> Stream.of("c2", "a1", "a2", "b0").filter(s -> s.startsWith("A"))
				.map(s -> s.toUpperCase());

		streamSupplier.get().forEach(s -> System.out.println("forEach: " + s));
		streamSupplier.get().count();
	}

}
