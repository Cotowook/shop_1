package com.shop.controller;

import com.shop.service.PostsService;
import com.shop.dto.PostsSaveRequestDto;
import com.shop.dto.PostsResponseDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shop.postAPI.Posts;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/community/posts")
public class PostsApiController {
    private final PostsService postsService;
    public PostsApiController(PostsService postsService) {
        this.postsService = postsService;
    }
    @PostMapping
    public Long createPost(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @GetMapping("/{id}")
    public PostsResponseDto getPost(@PathVariable Long id) {
        Posts post = postsService.findById(id);
        return new PostsResponseDto(post);
    }

    @GetMapping
    public List<PostsResponseDto> getAllPosts() {
        List<Posts> posts = postsService.findAll();
        List<PostsResponseDto> response = posts.stream()
                .map(post -> new PostsResponseDto(post.getAuthor(), post.getTitle()))
                .collect(Collectors.toList());
        return response;
    }
}

    /*
    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @GetMapping("/api/v1/posts/list")
    public List<PostsListResponseDto> findAll() {
        return postsService.findAllDesc();
    }
     */