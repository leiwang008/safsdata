package org.safs.data.resource;

import org.safs.data.model.Engine;
import org.springframework.stereotype.Component;

@Component
public class EngineResourceAssembler extends ResourceAssembler<Engine, EngineResource> {

	@Override
	public EngineResource toResource(Engine o) {
		EngineResource resource = new EngineResource(o);
		return resource;
	}
}
