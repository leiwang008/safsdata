package org.safs.data.resource;

import org.safs.data.model.Orderable;
import org.springframework.stereotype.Component;

@Component
public class OrderableResourceAssembler extends ResourceAssembler<Orderable, OrderableResource> {

	@Override
	public OrderableResource toResource(Orderable o) {
		OrderableResource resource = new OrderableResource(o);
		return resource;
	}
}
