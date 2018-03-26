package org.safs.data.resource;

import org.safs.data.model.Testsuite;
import org.springframework.stereotype.Component;

@Component
public class TestsuiteResourceAssembler extends ResourceAssembler<Testsuite, TestsuiteResource> {

	@Override
	public TestsuiteResource toResource(Testsuite o) {
		TestsuiteResource resource = new TestsuiteResource(o);
		return resource;
	}
}
