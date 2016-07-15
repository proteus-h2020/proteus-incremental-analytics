package com.treelogic.proteus.core.utils;



public class StringUtils {
	
	
	public static void checkString(String field) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("String cannot be null");
		}
	}
	
	public static void checkStrings(String... fields) {
		for (String field : fields) {
			checkString(field);
		}
	}

}
