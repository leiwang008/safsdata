//From https://dzone.com/articles/creating-a-rest-api-with-java-and-spring

package org.safs.data;

import org.safs.data.model.Status;
import org.safs.data.model.Testcase;
import org.safs.data.model.Teststep;
import org.safs.data.model.Testsuite;
import org.safs.data.repository.StatusRepository;
import org.safs.data.repository.TestcaseRepository;
import org.safs.data.repository.TeststepRepository;
import org.safs.data.repository.TestsuiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

@EnableEntityLinks
@EnableHypermediaSupport(type = HypermediaType.HAL)
@SpringBootApplication
@EntityScan("org.safs.data.model")
@EnableJpaRepositories("org.safs.data.repository")
public class SAFSDataApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(SAFSDataApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(SAFSDataApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SAFSDataApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(StatusRepository statusRep,
			                      TestcaseRepository testcaseRep,
			                      TeststepRepository teststepRep,
			                      TestsuiteRepository testsuiteRep){
		return (args) -> {
			if(statusRep.count()>0){
				log.debug("======================StatusRepository Data=========================");
				for(Status i: statusRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(testsuiteRep.count()>0){
				log.debug("======================TestsuiteRepository Data=========================");
				for(Testsuite i: testsuiteRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(testcaseRep.count()>0){
				log.debug("======================TestcaseRepository Data==========================");
				for(Testcase i: testcaseRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(teststepRep.count()>0){
				log.debug("======================TeststepRepository Data=============================");
				for(Teststep i: teststepRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
		};
	}

}