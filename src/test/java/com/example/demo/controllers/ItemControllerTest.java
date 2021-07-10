package com.example.demo.controllers;

import com.example.demo.model.persistence.entities.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
public class ItemControllerTest {

	@Mock
	ItemRepository itemRepositoryMocked;

	ItemController itemController;
	Item testItem = new Item();


	@Before
	public void setup(){
		itemController = new ItemController(itemRepositoryMocked);
		testItem = this.getTestItem();
		List<Item> itemList = new ArrayList<>();
		itemList.add(testItem);
		Mockito.when(itemRepositoryMocked.findAll()).thenReturn(itemList);
		Mockito.when(itemRepositoryMocked.findById(testItem.getId())).thenReturn(Optional.of(testItem));
		Mockito.when(itemRepositoryMocked.findByName(testItem.getName())).thenReturn(itemList);
	}


	@Test
	public void getItems() {
		final ResponseEntity<List<Item>> response = itemController.getItems();
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Item> retrievedItemList = response.getBody();
		Assert.assertEquals(retrievedItemList.size(), 1);
		Assert.assertEquals(retrievedItemList.get(0), this.testItem);
	}

	@Test
	public void getItemById() {
		final ResponseEntity<Item> response = itemController.getItemById(1L);
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Item retrievedItem = response.getBody();
		Assert.assertEquals(retrievedItem, testItem);
	}

	@Test
	public void getItemsByName() {
		final ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Item> retrievedItemList = response.getBody();
		Assert.assertEquals(retrievedItemList.size(), 1);
		Assert.assertEquals(retrievedItemList.get(0), this.testItem);
	}

	private Item getTestItem() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Item 1");
		item.setPrice(new BigDecimal(1));
		item.setDescription("Description");
		return item;
	}
}
