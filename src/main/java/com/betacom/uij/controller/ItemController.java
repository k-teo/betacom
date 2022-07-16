package com.betacom.uij.controller;


import com.betacom.uij.dto.request.ItemDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.exception.UserNotFoundException;
import com.betacom.uij.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> list() throws UserNotFoundException {
        return itemService.findAll();
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto> add(@RequestBody ItemDto item) throws UserNotFoundException {
        return itemService.add(item.getName());
    }
}
