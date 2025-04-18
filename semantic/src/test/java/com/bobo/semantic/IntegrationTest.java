package com.bobo.semantic;

public @interface IntegrationTest {

  Class<?>[] value() default {};

}
