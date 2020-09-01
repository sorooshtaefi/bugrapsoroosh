package com.vaadin.bugrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.bugrap.domain.BugrapRepository;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        BugrapRepository repository = new BugrapRepository("/Users/soroosh/bugrap/test-data");
        repository.populateWithTestData();
        SpringApplication.run(Application.class, args);
    }

}
