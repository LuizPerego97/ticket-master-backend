package com.github.luizperego97.ticket_master_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(
		scanBasePackages = {
				"com.github.luizperego97.ticket_master_backend",
				"com.github.luizperego97.shared_core"
		}
)
public class TicketMasterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketMasterBackendApplication.class, args);
	}
}