package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import com.shop.repository.ItemRepository;
import com.shop.repository.ItemRepositoryCustomImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;

import com.shop.dto.ItemImgDto;
import com.shop.service.ItemImgService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class ItemApiController {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    private ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemNm(item.getItemNm());
        itemDto.setPrice(item.getPrice());
        itemDto.setItemDetail(item.getItemDetail());
        itemDto.setSellStatCd(item.getItemSellStatus().toString());
        itemDto.setRegTime(item.getRegTime());
        itemDto.setUpdateTime(item.getUpdateTime());

        List<ItemImgDto> itemImages = itemImgService.getItemImagesByItemId(item.getId());
        itemDto.setImages(itemImages);

        return itemDto;
    }

    @Autowired
    public ItemApiController(ItemRepository itemRepository, ItemImgService itemImgService,
                             ItemRepositoryCustomImpl itemRepositoryCustom) {
        this.itemRepository = itemRepository;
        this.itemImgService = itemImgService;
    }
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> getItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }
    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) {
            ItemDto itemDto = convertToDto(item);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("image/{itemId}/{imageId}")
    public ResponseEntity<Resource> getItemImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        Resource imageResource = itemImgService.getItemImageResource(itemId, imageId);

        if (imageResource != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @GetMapping("/search/{itemName}")
    public ResponseEntity<List<ItemDto>> searchItemsByName(@PathVariable String itemName) {
        List<Item> items = itemRepository.findByItemNm(itemName);
        List<ItemDto> itemDtos = items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        itemDtos.forEach(itemDto -> {
            List<ItemImgDto> itemImages = itemImgService.getItemImagesByItemId(itemDto.getId());
            itemDto.setImages(itemImages);
        });

        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

}