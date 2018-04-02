package org.safs.data.resource;

import org.safs.data.model.Usage;
import org.springframework.stereotype.Component;

@Component
public class UsageResourceAssembler extends ResourceAssembler<Usage, UsageResource> {

	@Override
	public UsageResource toResource(Usage o) {
		UsageResource resource = new UsageResource(o);
		return resource;
	}
}
