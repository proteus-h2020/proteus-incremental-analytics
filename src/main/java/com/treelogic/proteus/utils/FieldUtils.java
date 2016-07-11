package com.treelogic.proteus.utils;

import java.lang.reflect.Field;

public class FieldUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object instance, String fName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field field = instance.getClass().getDeclaredField(fName);
		field.setAccessible(true);
		return (T)field.get(instance);
	}
}
