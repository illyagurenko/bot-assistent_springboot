package org.tgbot.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class AssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistantApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Устанавливаем часовой пояс для всего приложения
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
	}
}
