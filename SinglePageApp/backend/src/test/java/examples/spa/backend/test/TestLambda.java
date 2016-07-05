package examples.spa.backend.test;

import java.util.function.Function;

public class TestLambda {

	void testCall(String key,
			Function<String, String> mappingFunction) {
		if (key != null)
			mappingFunction.apply(key);
	}

	void doIt() throws Exception {
		testCall("asd", k -> { 
			System.out.println(k); 
			return null; 
		});
	}

	public static void main(String[] args) throws Exception {
		new TestLambda().doIt();
		System.out.println("Done.");
	}
}
