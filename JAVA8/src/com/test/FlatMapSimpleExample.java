package com.test;

import java.util.Optional;

class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}

public class FlatMapSimpleExample {

	public static void main(String[] args) {
		Outer outer = new Outer();
		outer.nested = new Nested();
		outer.nested.inner = new Inner();
		outer.nested.inner.foo = "Hello";
		
		
		
		
		Optional.of(outer)
	    .flatMap(o -> Optional.ofNullable(o.nested))
	    .flatMap(n -> Optional.ofNullable(n.inner))
	    .flatMap(i -> Optional.ofNullable(i.foo))
	    .ifPresent(System.out::println);
	}
	
}
