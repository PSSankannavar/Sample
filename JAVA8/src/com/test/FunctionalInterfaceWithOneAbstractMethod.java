package com.test;
/*
 * An interface which has Single Abstract Method and  can have more than one default methods ,can be called as Functional Interface.
 * Runnable, Comparator,Cloneable are some of the examples for Functional Interface. We can implement these Functional Interfaces by using Lambda expression.
 */
public class FunctionalInterfaceWithOneAbstractMethod {

	//Functional Interface without parameters
	public interface FunctionalInterfaceWithoutParameterTest{
		void display();
		}
	
	//Functional Interface with parameters
	public interface Addition{
		int add(int a ,int b);
		}
	
	

	//Functional Interface with parameters without parenthesis
	public interface Substraction{
		int sub(int a ,int b);
	}
	
	public static void main(String[] args) {
		// Old way using anonymous inner class
		FunctionalInterfaceWithoutParameterTest oldWay = new FunctionalInterfaceWithoutParameterTest() {
			public void display() {
				System.out.println("Display from old way");
			}
		};
	
		oldWay.display();
////////////////////////////////////////////////////////////////////////////////	
		//Using lambda expression
		FunctionalInterfaceWithoutParameterTest newWay = () ->{
			System.out.println("Display from new way");
		};
		
		newWay.display();
//////////////////////////////////////////////////////////////////////////////////
		
		Addition addition = (int a , int b) ->{
			return a+b;
		};
		
		int print = addition.add(4, 8);
		System.out.println("Addition " +print);
////////////////////////////////////////////////////////////////////////////////////
		
		
		Substraction substraction = (int a, int b) -> a - b;
		int printValue = substraction.sub(8, 4);
		System.out.println("Substraction " + printValue);
		
		
		
	}
}
