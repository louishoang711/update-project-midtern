package com.hcmute.bookstore.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfigListener_24162056 implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("Khong tim thay application.properties");
            }
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Khong doc duoc application.properties", exception);
        }

        event.getServletContext().setAttribute("appName", properties.getProperty("app.name", "BookStore"));
        event.getServletContext().setAttribute("studentId", properties.getProperty("student.id", "24162056"));
        event.getServletContext().setAttribute("examCode", properties.getProperty("exam.code", "02"));
    }
}
