package com.test;
/*
 * Lambda Expressions : 
 * 1.inline implementation of a functional interface
 * 2.eliminates the need of anonymous class
 * 3.you can refer to final variable or effectively final variable (which is assigned only once). Lambda expression throws a compilation error, if a variable is assigned a value the second time.
 * 4.Stream API
 */
public class LambdaExpressionsExample {

	 final static String salutation = "Hello! ";
	
	public interface MathOperation{
		int operation (int a , int b);		
	}
	
	public int Operate(int a, int b ,MathOperation operation){
		return operation.operation(a, b);
	}
	
	public interface SayMsg{
		void displayMsg(String name);
	}
	public static void main(String[] args) {
		
		MathOperation addition = (int a , int b) -> a + b ;
		MathOperation substract = (a,b)  -> a-b;
		MathOperation multiple = (a ,b) -> {return a*b;};
		
		LambdaExpressionsExample test = new LambdaExpressionsExample();
		System.out.println("Addition : " + test.Operate(8, 4, addition));
		System.out.println("Substract : " + 	test.Operate(8, 4, substract));
		System.out.println("multiple : " + 	test.Operate(8, 4, multiple));
		
		SayMsg msg = (String name) -> System.out.println(salutation + name);
		msg.displayMsg("World");
		
	}
	
}
