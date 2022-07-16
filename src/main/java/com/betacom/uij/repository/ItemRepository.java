package com.betacom.uij.repository;

import com.betacom.uij.model.Item;
import com.betacom.uij.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByOwner(User user);
}
