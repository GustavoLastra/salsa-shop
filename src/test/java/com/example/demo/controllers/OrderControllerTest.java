package com.example.demo.controllers;

import com.example.demo.model.dto.UpdateCartDto;
import com.example.demo.model.persistence.entities.Cart;
import com.example.demo.model.persistence.entities.Item;
import com.example.demo.model.persistence.entities.User;
import com.example.demo.model.persistence.entities.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

	@Mock
	UserRepository userRepositoryMocked;

	@Mock
	OrderRepository orderRepositoryMocked;


	OrderController orderController;
	User testuser = new User();
	UserOrder testUserOrder = new UserOrder();
	Cart testCart = new Cart();
	Item testItem = new Item();


	@Before
	public void setup(){
		orderController = new OrderController(userRepositoryMocked,orderRepositoryMocked);
		testuser = this.getTestUser();
		List<Item> testItems = getItems();
		testCart = this.getTestCart(testuser,testItems);
		testuser.setCart(testCart);
		testUserOrder = this.getTestUserOrder(testuser,testItems);
		List<UserOrder> userOrderList = new ArrayList<>();
		userOrderList.add(testUserOrder);

		Mockito.when(userRepositoryMocked.findByUsername(testuser.getUsername())).thenReturn(testuser);
		Mockito.when(orderRepositoryMocked.findByUser(testuser)).thenReturn(userOrderList);
	}




	@Test
	public void submit() {
		final ResponseEntity<UserOrder> response = orderController.submit(testuser.getUsername());
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		UserOrder submittedOrder = response.getBody();
		Assert.assertEquals(submittedOrder.getUser().getUsername(), testuser.getUsername());
		Assert.assertEquals(submittedOrder.getItems(), testUserOrder.getItems());
		Assert.assertEquals(submittedOrder.getTotal(), testUserOrder.getTotal());
	}

	@Test
	public void getOrdersForUser() {
		final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testuser.getUsername());
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		List<UserOrder> retrievedUserOrders = response.getBody();
		Assert.assertEquals(retrievedUserOrders.size(), 1);
		Assert.assertEquals(retrievedUserOrders.get(0), testUserOrder);
	}

	private User getTestUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("User A");
		user.setPassword("hashedPassword");
		return user;
	}

	private List<Item> getItems() {
		testItem = this.getTestItem();
		List<Item> items = new ArrayList<>();
		items.add(testItem);
		return items;
	}

	private UserOrder getTestUserOrder(User user,List<Item> items) {
		UserOrder userOrder = new UserOrder();
		userOrder.setId(1L);
		userOrder.setUser(user);
		userOrder.setItems(items);
		return userOrder;
	}

	private Cart getTestCart(User user, List<Item> items) {
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		cart.setItems(items);
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
