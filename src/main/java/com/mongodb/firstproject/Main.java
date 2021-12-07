package com.mongodb.firstproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository repository , MongoTemplate mongoTemplate){
		return args -> {
			Adress adress = new Adress("Turkey" , "İstanbul" , "3400");
			String email = "burakkurt72@gmail.com";
			Student student = new Student(
					"Burak",
					"Kurt",
					"burakkurt72@gmail.com",
					Gender.MALE,
					adress,
					List.of("Software" , "Test"),
					BigDecimal.TEN,
					LocalDateTime.now()
			);
			Query query = new Query();
			query.addCriteria(Criteria.where("email").is(email));
			List<Student> students = mongoTemplate.find(query , Student.class);
			if (students.size() > 1){
				throw new IllegalArgumentException("Found many students with email" + email);
			}
			if (students.isEmpty()){
				System.out.println("Inserting student" + student);
				repository.insert(student);
			}else{
				System.out.println(student + " already exists");
			}
		};
	}
}
