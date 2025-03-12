package edu.byui.apj.storefront.tutorial101;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Tutorial101Application {
    public static void main(String[] args) {
        SpringApplication.run(Tutorial101Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            log.debug("Logger class: " + log.getClass().getName());

            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            log.info("Customers found with findAll():");
            repository.findAll().forEach(customer -> log.info(customer.toString()));

            Customer customer = repository.findById(1L);
            log.info("Customer found with findById(1L):");
            log.info(customer.toString());

            log.info("Customer found with findByLastName('Bauer'):");
            repository.findByLastName("Bauer").forEach(bauer -> log.info(bauer.toString()));
        };
    }
}

