package online.testing.user.model.user.impl;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.hamcrest.CoreMatchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import online.testing.user.dto.UserPrincipal;
import online.testing.user.model.user.User;

class UserFactoryTest {
	private Mockery mockery;

	private UserPrincipal userPrincipal;
	private User mockUser;
	private ObjectId objectId;

	@BeforeEach
	void setUp() {
		mockery = new Mockery();

		objectId = new ObjectId("5f5cc2610b494a13c7282c79");

		mockUser = createUser();

		userPrincipal = UserPrincipal.create(mockUser);
	}

	private User createUser() {
		User u = mockery.mock(User.class);
		mockery.checking(new Expectations() {
			{
				oneOf(u).getId();
				will(returnValue(objectId.toString()));

				oneOf(u).getEmail();
				will(returnValue("depa@gmail.com"));

				oneOf(u).getPassword();
				will(returnValue("xxx"));

				oneOf(u).getAttributes();
				will(returnValue(new HashMap<>()));

				oneOf(u).getRole();
				will(returnValue("USER"));
			}
		});
		return u;
	}

	@Test
	public void testCreateLocalUser() {
		String registrationId = "local";

		User user = UserFactory.create(userPrincipal, registrationId);

		Assert.assertThat(user.getProvider(), CoreMatchers.equalTo(registrationId));
	}

	@Test
	public void testCreateGoogleUser() {
		String registrationId = "google";

		User user = UserFactory.create(userPrincipal, registrationId);

		Assert.assertThat(user.getProvider(), CoreMatchers.equalTo(registrationId));
	}

	@Test
	public void testCreateFacebookUser() {
		String registrationId = "facebook";

		User user = UserFactory.create(userPrincipal, registrationId);

		Assert.assertThat(user.getProvider(), CoreMatchers.equalTo(registrationId));
	}
}
