package com.example.demo.controllers;

import com.example.demo.model.dto.CreateUserDto;
import com.example.demo.model.persistence.entities.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

	@Mock
	UserRepository userRepositoryMocked;

	@Mock
	CartRepository cartRepositoryMocked;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoderMocked;


	UserController userController;
	User testuser = new User();


	@Before
	public void setup(){
		userController = new UserController(userRepositoryMocked,cartRepositoryMocked,bCryptPasswordEncoderMocked);
		testuser = this.getTestUser();
		Mockito.when(userRepositoryMocked.findByUsername(testuser.getUsername())).thenReturn(testuser);
		Mockito.when(userRepositoryMocked.findById(testuser.getId())).thenReturn(Optional.of(testuser));
		Mockito.when(bCryptPasswordEncoderMocked.encode("testPassword")).thenReturn("hashedPassword");
	}


	@Test
	public void createUser() {
		final ResponseEntity<User> response = userController.createUser(getCreateUserDto());
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		User createdUser = response.getBody();
		Assert.assertEquals("hashedPassword", createdUser.getPassword());
	}

	@Test
	public void findUserById() {
		final ResponseEntity<User> response = userController.findById(testuser.getId());
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		User createdUser = response.getBody();
		Assert.assertEquals("User A", createdUser.getUsername());
		Assert.assertEquals("hashedPassword", createdUser.getPassword());
	}

	@Test
	public void findByUserName() {
		final ResponseEntity<User> response = userController.findByUserName(testuser.getUsername());
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		User createdUser = response.getBody();
		Assert.assertEquals("User A", createdUser.getUsername());
		Assert.assertEquals("hashedPassword", createdUser.getPassword());
	}


	private User getTestUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("User A");
		user.setPassword("hashedPassword");
		return user;
	}

	private CreateUserDto getCreateUserDto() {
		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setUsername("User A");
		createUserDto.setPassword("testPassword");
		createUserDto.setConfirmPassword("testPassword");
		return createUserDto;
	}
}
