package ru.netology.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/*")
public class MainServlet extends HttpServlet {
    private static final String API_POSTS_PATH = "/api/posts";
    private static final String API_POSTS_ID_REGEX = "/api/posts/\\d+";

    @Autowired
    private PostController controller;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        if (controller == null) {
            throw new IllegalStateException("PostController not injected!");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final var path = req.getRequestURI().substring(req.getContextPath().length());
            final var method = req.getMethod();

            if (method.equals("GET") && path.equals(API_POSTS_PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(API_POSTS_ID_REGEX)) {
                final var id = extractId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(API_POSTS_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(API_POSTS_ID_REGEX)) {
                final var id = extractId(path);
                controller.removeById(id, resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long extractId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}