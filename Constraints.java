package com.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//定义字段的约束
public @interface Constraints {
	boolean primaryKey() default false;
	boolean allowNull() default true;
	boolean unique() default false;
}
