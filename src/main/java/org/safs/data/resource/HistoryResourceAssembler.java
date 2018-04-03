package org.safs.data.resource;

import org.safs.data.model.History;
import org.springframework.stereotype.Component;

@Component
public class HistoryResourceAssembler extends ResourceAssembler<History, HistoryResource> {

	@Override
	public HistoryResource toResource(History o) {
		HistoryResource resource = new HistoryResource(o);
		return resource;
	}
}
