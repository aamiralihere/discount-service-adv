package com.retailstore.discountservice;

import com.retailstore.discountservice.constant.ItemType;
import com.retailstore.discountservice.model.Item;
import com.retailstore.discountservice.repository.ItemRepository;
import com.retailstore.discountservice.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    @Test
    public void testCreateItem() {
        Item item = new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC);

        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.createItem(item);

        assertEquals(savedItem.getId(), item.getId());
    }

    @Test
    public void testGetItem() {
        Item item = new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC);

        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.createItem(item);

        when(itemRepository.findById(savedItem.getId())).thenReturn(Optional.of(savedItem));

        Item foundItem = itemService.getItem(savedItem.getId());

        assertEquals(savedItem.getId(), foundItem.getId());
    }

    @Test
    public void testUpdateUser() {
        Item item = new Item(1L, "Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC);

        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.createItem(item);

        when(itemRepository.findById(savedItem.getId())).thenReturn(Optional.of(savedItem));

        Item newItem = itemService.getItem(savedItem.getId());
        newItem.setItemName("Toaster 2");

        when(itemRepository.save(newItem)).thenReturn(newItem);

        Item updatedItem = itemService.updateItem(newItem);

        assertEquals(newItem.getItemName(), updatedItem.getItemName());
    }
}
