package com.bobo.semantic;

public @interface UnitTest {

  Class<?> value() default Object.class;

}
