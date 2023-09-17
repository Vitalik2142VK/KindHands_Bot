package tg.kindhands_bot.kindhands;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KindhandsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KindhandsApplication.class, args);
	}

}
