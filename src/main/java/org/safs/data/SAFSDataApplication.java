//From https://dzone.com/articles/creating-a-rest-api-with-java-and-spring
/**
 * History:
 * WangLei 28 MAR, 2018	Modified method demo(): print the data from repository in a general way.
 */
package org.safs.data;

import java.util.ArrayList;
import java.util.List;

import org.safs.data.repository.EngineRepository;
import org.safs.data.repository.FrameworkRepository;
import org.safs.data.repository.MachineRepository;
import org.safs.data.repository.OrderableRepository;
import org.safs.data.repository.StatusRepository;
import org.safs.data.repository.TestcaseRepository;
import org.safs.data.repository.TestcycleRepository;
import org.safs.data.repository.TeststepRepository;
import org.safs.data.repository.TestsuiteRepository;
import org.safs.data.repository.UsageRepository;
import org.safs.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
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

	@Autowired
	private FrameworkRepository frameworkRep;
	@Autowired
	private EngineRepository engineRep;
	@Autowired
	private MachineRepository machineRep;
	@Autowired
	private UserRepository userRep;
	@Autowired
	private UsageRepository usageRep;

	@Autowired
	private OrderableRepository orderableRep;
	@Autowired
	private StatusRepository statusRep;
	@Autowired
	private TestcycleRepository testcycleRep;
	@Autowired
	private TestsuiteRepository testsuiteRep;
	@Autowired
	private TestcaseRepository testcaseRep;
	@Autowired
	private TeststepRepository teststepRep;

	@Bean
	public CommandLineRunner demo(){

		List<CrudRepository> repositories = new ArrayList<CrudRepository>();
		repositories.add(frameworkRep);
		repositories.add(engineRep);
		repositories.add(machineRep);
		repositories.add(userRep);
//		repositories.add(usageRep);

		repositories.add(orderableRep);
		repositories.add(statusRep);
		repositories.add(testcycleRep);
		repositories.add(testsuiteRep);
		repositories.add(testcaseRep);
		repositories.add(teststepRep);

		return (args) -> {
			if(log.isDebugEnabled()){
				for(CrudRepository repo: repositories){
					if(repo.count()>0){
//						Proxy.getInvocationHandler(repo);
						//repo is a proxy class, repo.getClass().getName() will not give the real class name
						log.debug("======================"+repo.getClass().getName()+" Data=========================");
						for(Object i: repo.findAll()){
							log.debug(i.toString());
						}
						log.debug("============================================================");
					}
				}

			}
		};
	}

}