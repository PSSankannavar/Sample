package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Student {
    String name;
    List<Address> address = new ArrayList<>();

    Student(String name) {
        this.name = name;
    }
}

class Address {
    String name;

    Address(String name) {
        this.name = name;
    }
}

public class FlatMap {
	
	
	public static void main(String[] args) {
		List<Student> students = new ArrayList<>();
		IntStream.range(1, 4).forEach(p ->students.add( new Student("Student " + p)));
		
		students.forEach(s -> {
			IntStream.range(1, 4).forEach(n -> s.address.add(new Address(s.name +" <- Address " + n )));
		});
		
		 List<Address> list = students.stream().flatMap(f -> f.address.stream()).collect(Collectors.toList());
		list.stream().forEach(n ->System.out.println(n.name));

	}
	
	
	
	
	

}
