package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import com.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/items")
public class ItemApiController {
    private final ItemRepository itemRepository;
    @Autowired
    public ItemApiController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) {
            ItemDto itemDto = convertToDto(item);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemNm(item.getItemNm());
        itemDto.setPrice(item.getPrice());
        itemDto.setItemDetail(item.getItemDetail());
        itemDto.setSellStatCd(item.getItemSellStatus().toString());
        itemDto.setRegTime(item.getRegTime());
        itemDto.setUpdateTime(item.getUpdateTime());
        return itemDto;
    }
}


