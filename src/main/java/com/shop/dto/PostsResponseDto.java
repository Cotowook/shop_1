package com.shop.dto;

import lombok.Getter;
import com.shop.postAPI.Posts;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
    public PostsResponseDto(String author, String title) {
        this.author = author;
        this.title = title;
    }
}
