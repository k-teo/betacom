package com.betacom.uij.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.betacom.uij.dto.request.ItemDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import com.betacom.uij.exception.UserNotFoundException;
import com.betacom.uij.model.Item;
import com.betacom.uij.model.User;
import com.betacom.uij.repository.ItemRepository;
import com.betacom.uij.repository.UserRepository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest extends AbstractUnitTestWithMockUser {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ItemService itemService;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    @BeforeEach
    void setUp() {
        super.mockSecurity();
    }

    @Test
    void testFindAll() throws UserNotFoundException {
        //given
        User user = buildUser("userName");
        Item item = buildItem(user);
        ItemDto itemDto = new ItemDto(item.getUuid().toString(), "itemName");
        when(itemRepository.findByOwner(any())).thenReturn(List.of(item));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        //when
        ResponseEntity<List<ItemDto>> result = itemService.findAll();

        //then
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), hasSize(1));
        assertThat(result.getBody(), hasItem(itemDto));
        verify(itemRepository, times(1)).findByOwner(any());
        verify(userRepository, times(1)).findByLogin(any());
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    void testFindAllShouldThrowUserNotFoundException() {
        //given
        when(userRepository.findByLogin(any())).thenReturn(Optional.empty());

        //when
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> itemService.findAll());

        //then
        assertThat(thrown, isA(UserNotFoundException.class));
        assertThat(thrown.getMessage(), is(MessageResponseType.USER_NOT_FOUND.getMessage()));
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    void testAdd() throws UserNotFoundException {
        //given
        String itemName = "itemName";
        String userName = "username";
        User user = buildUser(userName);

        when(userRepository.findByLogin(any())).thenReturn(Optional.of(user));

        //when
        ResponseEntity<MessageResponseDto> result = itemService.add(itemName);

        //then
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getDescription(), is(MessageResponseType.ITEM_CREATED.getMessage()));
        verify(itemRepository, times(1)).save(itemCaptor.capture());
        Item savedItem = itemCaptor.getValue();
        assertThat(savedItem, is(notNullValue()));
        assertThat(savedItem.getName(), is(itemName));
        assertThat(savedItem.getUuid(), is(nullValue()));
        assertThat(savedItem.getOwner(), is(notNullValue()));
        assertThat(savedItem.getOwner(), is(user));
        verify(userRepository, times(1)).findByLogin(any());
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    void testAddShouldThrowUserNotFoundException() {
        //given
        String itemName = "itemName";
        when(userRepository.findByLogin(any())).thenReturn(Optional.empty());

        //when
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> itemService.add(itemName));

        //then
        assertThat(thrown, isA(UserNotFoundException.class));
        assertThat(thrown.getMessage(), is(MessageResponseType.USER_NOT_FOUND.getMessage()));
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    private Item buildItem(User user) {
        Item item = new Item();
        item.setUuid(UUID.randomUUID());
        item.setName("itemName");
        item.setOwner(user);
        return item;
    }

    private User buildUser(String userName) {
        User user = new User();
        user.setLogin(userName);
        user.setPassword("password");
        user.setUuid(UUID.randomUUID());
        return user;
    }
}
