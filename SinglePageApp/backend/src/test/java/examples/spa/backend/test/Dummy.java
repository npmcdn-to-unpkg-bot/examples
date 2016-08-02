package examples.spa.backend.test;

public class Dummy {

	void doIt() throws Exception {
		Object o = null;
		String s = String.valueOf(o);
		System.out.println(s);
	}

	public static void main(String[] args) throws Exception {
		new Dummy().doIt();
		System.out.println("Done.");
	}
}
