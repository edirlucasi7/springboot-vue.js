package com.carrinho.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrinho.server.model.Item;
import com.carrinho.server.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	public List<Item> getAllItem() {		
		return itemRepository.findAll();
	}
}
