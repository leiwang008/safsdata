package org.safs.data.resource;

import org.safs.data.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends ResourceAssembler<User, UserResource> {

	@Override
	public UserResource toResource(User o) {
		UserResource resource = new UserResource(o);
		return resource;
	}
}
