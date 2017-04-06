package com.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Person {
    String name;
    Double salary;
    int age;
   

    Person(String name,Double salary, int age) {
        this.name = name;
        this.age = age;
        this.salary= salary;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}


public class CustomizedObject {
	
	public static void main(String[] args) {
		
		List<Person> persons =
			    Arrays.asList(
			        new Person("Max", 12000.0,18),
			        new Person("Peter", 12000.0,23),
			        new Person("Pamela", 21000.0,23),
			        new Person("Pamela", 23000.0,12));
		
		//built-in collectors
		
		List<Person> filtered =
			    persons
			        .stream()
			        .filter(p -> p.name.startsWith("P"))
			        .collect(Collectors.toList());
				System.out.println("starts with P : " +filtered);
		
		Map<Double, List<Person>> personsByAge = persons.stream().collect(Collectors.groupingBy(n -> n.salary));
		System.out.println("Grouped by age :"+personsByAge);
		
		personsByAge.forEach((i,k) ->System.out.println("Age : " +i +" Name : " + k));
		
		// mapped keys must be unique, otherwise an IllegalStateException is thrown.You can optionally pass a merge function as an additional parameter to bypass the exception
		Map<Double, String> map = persons.stream().collect(Collectors.toMap(p -> p.salary, p -> p.name,(name1, name2) -> name1 + ";" + name2));
		System.out.println("Map : " + map);
		
		
		String phrase = persons
			    .stream()
			    .filter(p -> p.age == 18)
			    .map(p -> p.name)
			    .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
		System.out.println(phrase);
		
		
		//build our own special collector
		Collector<Person, StringJoiner, String> personNameCollector =
			    Collector.of(
			        () -> new StringJoiner(" | "),          // supplier
			        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
			        (j1, j2) -> j1.merge(j2),               // combiner
			        StringJoiner::toString); 
		String names = persons.stream().collect(personNameCollector);

			System.out.println(names);
	
		persons.stream().reduce((p1,p2) -> p1.age >p2.age ? p1 :p2).ifPresent(System.out::println);
		Person result = persons.stream().reduce(new Person("", 0.0,0),(p1,p2) ->{
			p1.name +=p2.name;
			p1.age +=p2.age;
			return p1;
		});
		System.out.println(result.name + " age is " + result.age);
	
	



	
	}}


