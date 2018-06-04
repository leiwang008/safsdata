package org.safs.data.resource;

import org.safs.data.model.HistoryEngine;
import org.springframework.stereotype.Component;

@Component
public class HistoryEngineResourceAssembler extends ResourceAssembler<HistoryEngine, HistoryEngineResource> {

	@Override
	public HistoryEngineResource toResource(HistoryEngine o) {
		HistoryEngineResource resource = new HistoryEngineResource(o);
		return resource;
	}
}
