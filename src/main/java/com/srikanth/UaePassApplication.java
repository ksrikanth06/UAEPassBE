package com.srikanth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class UaePassApplication {

	private static final Logger logger = LoggerFactory.getLogger(UaePassApplication.class);

	public static void main(String[] args) {
		logger.info("Starting UAE Pass Application...");
		try {
			SpringApplication.run(UaePassApplication.class, args);
			logger.info("UAE Pass Application started successfully");
		} catch (Exception e) {
			logger.error("Failed to start UAE Pass Application: ", e);
			throw e;
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("UAE Pass Application is ready to serve requests");
		logger.info("Application is running on http://localhost:8000");
		logger.info("Available endpoints:");
		logger.info("  GET /redirectURL?code=<code>&state=<state>");
		logger.info("  GET /getToken?authCode=<authCode>");
		logger.info("  GET /getUserInfo?authToken=<authToken>");
	}
}
