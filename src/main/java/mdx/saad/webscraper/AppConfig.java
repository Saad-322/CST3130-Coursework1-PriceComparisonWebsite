package mdx.saad.webscraper;

import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {

	@Bean
	public Hibernate hibernateInstance() {
		Hibernate tmpInstance = new Hibernate();
		tmpInstance.init();
		return tmpInstance;
	}
	
	@Bean
	public Website1 RightMove() {
		Website1 tmpWebsite1 = new Website1();
		tmpWebsite1.setPostCode("");
		tmpWebsite1.setOutCode("");
		tmpWebsite1.setCrawlDelay(15);
		tmpWebsite1.setHibernateInstance(hibernateInstance());
		return tmpWebsite1;
	}
	
	@Bean
	public Website2 OpenRent() {
		Website2 tmpWebsite2 = new Website2();
		tmpWebsite2.setPostCode("");
		tmpWebsite2.setCrawlDelay(15);
		tmpWebsite2.setHibernateInstance(hibernateInstance());
		return tmpWebsite2;
	}
	
	@Bean
	public Website3 Zoopla() {
		Website3 tmpWebsite3 = new Website3();
		tmpWebsite3.setPostCode("");
		tmpWebsite3.setCrawlDelay(15);
		tmpWebsite3.setHibernateInstance(hibernateInstance());
		return tmpWebsite3;
	}
	
	@Bean
	public Website4 Gumtree() {
		Website4 tmpWebsite4 = new Website4();
		tmpWebsite4.setPostCode("");
		tmpWebsite4.setCrawlDelay(15);
		tmpWebsite4.setHibernateInstance(hibernateInstance());
		return tmpWebsite4;
	}

}
