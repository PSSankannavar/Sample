package com.test;


public class FunctionalInterfaceWithOneAbstractMethodAndDefaultMethods {

	public interface FunctionalInterfaceTest{
		void abstractMethod();
		default void defaultMethod(){
			System.out.println("Display from default method");
		}
	}
	
	
	public static void main(String[] args) {
		
		FunctionalInterfaceTest interfaceTest = () ->{
			System.out.println("Display from abstract method");
		};
		
		interfaceTest.abstractMethod();
		interfaceTest.defaultMethod();
	}
}
