package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public synchronized Post save(Post post) {
        if (post.getId() == 0) {
            // new post
            long newId = counter.getAndIncrement();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        } else {
            // Existing post
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
                return post;
            } else {
                throw new NotFoundException("Post with id " + post.getId() + " not found");
            }
        }
    }

    public synchronized void removeById(long id) {
        if (!posts.containsKey(id)) {
            throw new NotFoundException("Post with id " + id + " not found");
        }
        posts.remove(id);
    }
}
