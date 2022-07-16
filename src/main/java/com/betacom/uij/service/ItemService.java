package com.betacom.uij.service;


import com.betacom.uij.dto.request.ItemDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import com.betacom.uij.exception.UserNotFoundException;
import com.betacom.uij.model.Item;
import com.betacom.uij.model.User;
import com.betacom.uij.repository.ItemRepository;
import com.betacom.uij.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<List<ItemDto>> findAll() throws UserNotFoundException {
        User user = getUserFromContext();
        log.info("Fetching items for user: {}", user.getLogin());
        List<Item> items = itemRepository.findByOwner(user);
        List<ItemDto> itemDtos = items.stream()
                                        .map(item -> new ItemDto(item.getUuid().toString(), item.getName()))
                                        .collect(Collectors.toList());

        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    public ResponseEntity<MessageResponseDto> add(String itemName) throws UserNotFoundException {
        User user = getUserFromContext();
        itemRepository.save(new Item(null, user, itemName));
        log.info("Add new item: {} for user: {}", itemName, user.getLogin());

        return new ResponseEntity<>(new MessageResponseDto(MessageResponseType.ITEM_CREATED.getMessage()), HttpStatus.OK);
    }

    private User getUserFromContext() throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByLogin(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
    }

}
