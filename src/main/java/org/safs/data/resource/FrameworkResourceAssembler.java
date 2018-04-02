package org.safs.data.resource;

import org.safs.data.model.Framework;
import org.springframework.stereotype.Component;

@Component
public class FrameworkResourceAssembler extends ResourceAssembler<Framework, FrameworkResource> {

	@Override
	public FrameworkResource toResource(Framework o) {
		FrameworkResource resource = new FrameworkResource(o);
		return resource;
	}
}
