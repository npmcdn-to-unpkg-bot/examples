package examples.spa.backend.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import examples.spa.backend.component.UtilsService;
import examples.spa.backend.myRest.MyRestConfig;
import examples.spa.backend.myRest.MyRestConfigurer;

public class TestMyConfigurer {

	void doIt() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("TestMyConfigurer-spring.xml", getClass());
		MyRestConfigurer myRestConfigurer = appContext.getBean(MyRestConfigurer.class);
		MyRestConfig config = myRestConfigurer.getMyRestConfig();
		UtilsService utils = appContext.getBean(UtilsService.class);
		
		System.out.println(utils.toJson(config));
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new TestMyConfigurer().doIt();
		System.out.println("Done.");
	}
}
