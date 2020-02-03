package com.jumia.validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jumia.validator.config.EnableJumia;

@EnableJumia
@SpringBootApplication
public class JumiaTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JumiaTestApplication.class, args);
	}

//	@Bean
//	CommandLineRunner initialize(CustomerRepository customerRepository) {
//		return args -> {
//			Stream.of("Jackson", "Victoria", "Jair").forEach(name -> {
//				Customer customer = new Customer(name, "(258) 846565883");
//				customerRepository.save(customer);
//			});
//			customerRepository.findAll().forEach(System.out::println);
//		};
//	}
}
