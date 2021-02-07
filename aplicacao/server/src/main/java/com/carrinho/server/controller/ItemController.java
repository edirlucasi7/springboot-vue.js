package com.carrinho.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrinho.server.model.Item;
import com.carrinho.server.service.ItemService;

@RestController
@RequestMapping("")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping("/items")
	public ResponseEntity<Page<Item>> obterItems(@RequestParam(value = "page", defaultValue = "0") Integer page,
			 									 @RequestParam(value = "size", defaultValue = "4") Integer size) {
		Page<Item> item = itemService.getAllItem(PageRequest.of(page, size));
		return ResponseEntity.ok(item);
	}
	
	@PostMapping("/cadastrarItem")
	public ResponseEntity<Item> inserirItem(@RequestBody Item item) {
		Item inserirItem = itemService.postItem(item);
		return ResponseEntity.ok(inserirItem);
	}
	
	@PutMapping("/atualizarItem")
	public ResponseEntity<String> atualizarItem(@RequestBody Item item) {
		return ResponseEntity.ok(itemService.updateItem(item));
	}
	
	@DeleteMapping("/apagarItem/{id}")
	public ResponseEntity<Item> apagarItem(@PathVariable("id") Integer id) {
		Item item = itemService.getItemPorId(id);
		if (item != null) {
			itemService.deleteItem(id);
			return ResponseEntity.ok(item);
		}
		return ResponseEntity.notFound().build();
	}
	
}
