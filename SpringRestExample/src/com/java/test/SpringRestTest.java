package com.java.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java.entity.Movie;

public class SpringRestTest {
	public static void main(String[] args) {

		/*
		 * Application context provides 
		 * 1.Bean factory methods for accessing application components
		 * 2.The ability to load file resources in a generic fashion
		 */
		ClassPathXmlApplicationContext applicationContextTest = new ClassPathXmlApplicationContext("bean-context.xml");
		Movie movie = (Movie) applicationContextTest.getBean(Movie.class);
		System.out.println(movie.getName());
	}
}
