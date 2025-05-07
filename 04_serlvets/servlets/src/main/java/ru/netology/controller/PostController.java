package ru.netology.controller;

import org.springframework.stereotype.Controller;
import ru.netology.exception.NotFoundException;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse resp) throws IOException {
        resp.getWriter().write("All posts");
    }

    public void getById(long id, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("Post with ID: " + id);
    }

    public void save(Reader body, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("Post saved");
    }

    public void removeById(long id, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("Post deleted: " + id);
    }
}