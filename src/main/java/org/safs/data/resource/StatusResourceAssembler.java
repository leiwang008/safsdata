package org.safs.data.resource;

import org.safs.data.model.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusResourceAssembler extends ResourceAssembler<Status, StatusResource> {

	@Override
	public StatusResource toResource(Status o) {
		StatusResource resource = new StatusResource(o);
		return resource;
	}
}
