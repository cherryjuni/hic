package biz.btsoft.hicapital;

public class HelloWorld {
	
	private int age;
	public String name;
	protected int firstName;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World!!");

	}
	
	public void method() {
		System.out.println("test");
	}

	private void methodPri() {
		System.out.println("method - private..");
	}
	
	protected void mothodProtected() {
		System.out.println("method - protected..");
	}
}
