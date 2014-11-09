package com.cement.test;

public class GenericClass {

	public static class MyGenericClass<T> {
		public void print() throws Exception {
			Class clazz = getClass();
			System.out.println(clazz.getSimpleName());
		}
	}
	
	void doIt() throws Exception {
		new MyGenericClass<String>().print();
	}

	public static void main(String[] args) throws Exception {
		new GenericClass().doIt();
		System.out.println("Done.");
	}
}
