package com.carrinho.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrinho.server.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{

}
