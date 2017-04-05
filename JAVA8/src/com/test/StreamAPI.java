package com.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 
 * @author psankann
 *Stream API : operational pipeline
 *
 */
public class StreamAPI {

	public static void main(String[] args) {
		
		List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
		
		
		List<String> filtered =strings.stream().filter(string -> !string.equals("abc")).collect(Collectors.toList());
		//System.out.println(filtered);
		
		
		//forEach 
		//strings.stream().forEach(System.out::println);
		//strings.stream().findFirst().ifPresent(System.out :: println);
		
		//Arrays.stream(new int[] {1, 2, 3}).average().ifPresent(System.out::println);
		//Arrays.stream(new String[]{"1","2","3"}).mapToInt(Integer :: parseInt).max().ifPresent(System.out :: println);
		
		//Arrays.stream(new int[]{1,2,3}).mapToObj(n -> "a" + n).forEach(System.out :: println);
		
		
		//Arrays.stream(new Double[]{1.0,2.0,3.0}).mapToInt(Double ::intValue).mapToObj(n -> "a" + n).forEach(System.out::println);
		//Arrays.stream(new Double[] { 1.0, 2.0, 3.0 }).mapToInt(Double::intValue).mapToObj(n -> {System.out.println("mapToObj" + n );return "a" + n;}).forEach(System.out::println);

		
		
		//
		Supplier<Stream<String>> streamSupplier =()->Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));
		streamSupplier.get().anyMatch(s -> true);
		streamSupplier.get().noneMatch(s -> true);  
		
		
	}
}
