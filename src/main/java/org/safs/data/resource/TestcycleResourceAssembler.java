package org.safs.data.resource;

import org.safs.data.model.Testcycle;
import org.springframework.stereotype.Component;

@Component
public class TestcycleResourceAssembler extends ResourceAssembler<Testcycle, TestcycleResource> {

	@Override
	public TestcycleResource toResource(Testcycle o) {
		TestcycleResource resource = new TestcycleResource(o);
		return resource;
	}
}
