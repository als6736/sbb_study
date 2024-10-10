package com.mysite.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@EnableAsync
public class SbbApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SbbApplication.class, args);
		SpringApplication app = new SpringApplication(SbbApplication.class);
		app.addListeners((ApplicationListener<ApplicationFailedEvent>) event -> {
			Throwable ex = event.getException();
			System.err.println("Application failed due to: " + ex.getMessage());
		});
		app.run(args);
	}

}
