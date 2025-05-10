package ru.netology.initializer;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import ru.netology.AppConfig;

import javax.servlet.ServletContext;

public class ApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // создаем контекст
        final var context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(servletContext);
        context.register(AppConfig.class);
        context.refresh();

        // настраиваем фильтр UTF-8
        final var filter = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter("UTF-8", true));
        filter.addMappingForUrlPatterns(null, true, "/*");

        // регистрируем DispatcherServlet
        final var servlet = new DispatcherServlet(context);
        final var registration = servletContext.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
