package com.carrinho.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrinho.server.model.Item;
import com.carrinho.server.service.ItemService;

@RestController
@RequestMapping("")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping("/items")
	public ResponseEntity<List<Item>> obterItemPorId() {
		List<Item> item = itemService.getAllItem();
		return ResponseEntity.ok(item);
	}
	
	@PostMapping("/cadastrarItem")
	public ResponseEntity<Item> inserirItem(@RequestBody Item item) {
		Item inserirItem = itemService.postItem(item);
		return ResponseEntity.ok(inserirItem);
	}
	
}
