package com.jsalva.gymsystem.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class GymWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create Spring application context
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        // Register your configuration classes
        context.register(AppConfig.class, WebConfig.class);

        // Add ContextLoaderListner to the ServletContext which will be responsible to load the application context
        servletContext.addListener(new ContextLoaderListener(context));

        // Create and register DispatcherServlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",  new DispatcherServlet(context));

        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");

        //add specific encoding (e.g. UTF-8) via CharacterEncodingFilter
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");
    }
}

