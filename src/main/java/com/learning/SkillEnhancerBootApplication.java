package com.learning;

import com.learning.repository.StudentRepository;
import com.learning.utility.email.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SpringBootApplication


public class SkillEnhancerBootApplication {

@Autowired
private EmailSender emailSender;

	public static void main(String[] args) {
		SpringApplication.run(SkillEnhancerBootApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


//	@Override
//	public void run(String... args) throws Exception {
//		//System.out.println(studentRepository.findAllName());
//		//System.out.println(studentRepository.findAllContactDetails());
//		System.out.println(studentRepository.findAllEmails());
//
//	}
}
