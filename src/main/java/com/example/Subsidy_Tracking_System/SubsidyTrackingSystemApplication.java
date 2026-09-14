package com.example.Subsidy_Tracking_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SubsidyTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubsidyTrackingSystemApplication.class, args);
	}

}
