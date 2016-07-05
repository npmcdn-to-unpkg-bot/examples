package examples.spa.backend.test;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestSpringStandaloneApp {

	void doIt() throws Exception {
		FileSystemXmlApplicationContext appContext = new FileSystemXmlApplicationContext("classpath:spring.xml");
		MessageSource ms = (MessageSource) appContext.getBean("messageSource");
		//MessageSource ms = appContext.getBean(MessageSource.class);
		Locale locale = Locale.ENGLISH;
//		locale = new Locale("bg", "");
		String str = ms.getMessage("name.beginsWithZZ", null, locale);
		System.out.println(str);
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new TestSpringStandaloneApp().doIt();
		System.out.println("Done.");
	}
}
