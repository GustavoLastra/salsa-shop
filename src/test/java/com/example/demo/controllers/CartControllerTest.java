package com.example.demo.controllers;

import com.example.demo.model.dto.CreateUserDto;
import com.example.demo.model.dto.UpdateCartDto;
import com.example.demo.model.persistence.entities.Cart;
import com.example.demo.model.persistence.entities.Item;
import com.example.demo.model.persistence.entities.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

	@Mock
	UserRepository userRepositoryMocked;

	@Mock
	CartRepository cartRepositoryMocked;

	@Mock
	ItemRepository itemRepositoryMocked;


	CartController cartController;
	User testuser = new User();
	Cart testCart = new Cart();
	Item testItem = new Item();


	@Before
	public void setup(){
		cartController = new CartController(userRepositoryMocked,cartRepositoryMocked,itemRepositoryMocked);
		testuser = this.getTestUser();
		testCart = this.getTestCart(testuser);
		testuser.setCart(testCart);
		testItem = this.getTestItem();
		Mockito.when(userRepositoryMocked.findByUsername(testuser.getUsername())).thenReturn(testuser);
		Mockito.when(itemRepositoryMocked.findById(testItem.getId())).thenReturn(Optional.of(testItem));
	}


	@Test
	public void addTocart() {
		UpdateCartDto updateCartDto = getUpdateCartDto(testItem);
		final ResponseEntity<Cart> response = cartController.addToCart(updateCartDto);
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Cart createdCart = response.getBody();
		Assert.assertEquals(createdCart.getUser().getUsername(), updateCartDto.getUsername());
		Assert.assertEquals(createdCart.getItems().get(0).getId(), testItem.getId());
		Assert.assertEquals(createdCart.getTotal(), testItem.getPrice());
	}

	@Test
	public void removeFromcart() {
		UpdateCartDto updateCartDto = getUpdateCartDto(testItem);
		final ResponseEntity<Cart> response = cartController.removeFromCart(updateCartDto);
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Cart updatedCart = response.getBody();
		Assert.assertEquals(updatedCart.getItems().size(), 0);
	}

	private User getTestUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("User A");
		user.setPassword("hashedPassword");
		return user;
	}

	private Cart getTestCart(User user) {
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		return cart;
	}

	private Item getTestItem() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Item 1");
		item.setPrice(new BigDecimal(1));
		item.setDescription("Description");
		return item;
	}

	private UpdateCartDto getUpdateCartDto(Item testItem) {
		UpdateCartDto updateCartDto = new UpdateCartDto();
		updateCartDto.setUsername("User A");
		updateCartDto.setQuantity(1);
		updateCartDto.setItemId(testItem.getId());
		return updateCartDto;
	}
}
