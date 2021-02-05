package com.carrinho.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carrinho.server.model.Item;
import com.carrinho.server.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	public Page<Item> getAllItem(Pageable pageable) {		
		return itemRepository.findAll(pageable);
	}
	
	public Item postItem(Item item) {
		return itemRepository.save(item);
	}
	
	public Item getItemPorId(Integer id) {
		Optional<Item> optional =  itemRepository.findById(id);
		if (optional.isPresent()) {
			Item item = optional.get();
			return item;
		}
		return null;
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
	
	public void deleteItem(Integer id) {
		itemRepository.deleteById(id);
	}
	
}
