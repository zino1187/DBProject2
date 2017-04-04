package test;

import java.net.URL;

public class TestMain {
	public TestMain() {
		URL url=this.getClass().getResource("/");
		String path=url.getPath();
		
		System.out.println(url.getPath());
		System.out.println(url.getPath().substring(1, path.lastIndexOf("/")));
	}

	public static void main(String[] args) {
		new TestMain();

	}

}
