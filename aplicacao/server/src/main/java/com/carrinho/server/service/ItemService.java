package com.carrinho.server.service;

import java.util.List;
import java.util.Optional;

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
	
	public Item postItem(Item item) {
		return itemRepository.save(item);
	}
	
	public String updateItem(Item item) {
		Optional<Item> optional = itemRepository.findById(item.getId());
		if(optional.isPresent()) {
			Item itemExistente = optional.get();
			itemExistente.setCategory(item.getCategory());
			itemExistente.setDescription(item.getDescription());
			itemExistente.setPrice(item.getPrice());
			itemRepository.save(itemExistente);
			return "Atualizado com sucesso";
		}
		
		return "Não foi possível atualizar. Item não encontrado!";
	}
	
}
