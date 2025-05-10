package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public PostRepository() {
        // Инициализация тестовых данных без проверки существования
        posts.put(1L, new Post(1L, "First post"));
        posts.put(2L, new Post(2L, "Second post"));
        counter.set(3L); // Устанавливаем следующий ID
    }

    public List<Post> all() {
        return posts.values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        Post post = posts.get(id);
        return (post == null || post.isRemoved())
                ? Optional.empty()
                : Optional.of(post);
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = counter.getAndIncrement();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        }

        Post existing = posts.get(post.getId());
        if (existing == null || existing.isRemoved()) {
            throw new NotFoundException("Post not found or removed");
        }

        posts.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        Post post = posts.get(id);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }
        post.setRemoved(true);
    }
}
