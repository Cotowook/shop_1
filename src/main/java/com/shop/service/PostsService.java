package com.shop.service;

import com.shop.postAPI.Posts;
import com.shop.postAPI.PostsRepository;
import com.shop.dto.PostsListResponseDto;
import com.shop.dto.PostsResponseDto;
import com.shop.dto.PostsSaveRequestDto;
import com.shop.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public Posts findById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
    }

    public List<Posts> findAll() {
        return postsRepository.findAll();
    }
}
