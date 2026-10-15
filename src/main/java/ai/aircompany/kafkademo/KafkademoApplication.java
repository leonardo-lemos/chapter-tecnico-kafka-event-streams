package ai.aircompany.kafkademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkademoApplication {

	public static void main(String[] args) {
		System.setProperty("org.apache.avro.SERIALIZABLE_PACKAGES", "ai.aircompany.kafkademo.avro");

		SpringApplication.run(KafkademoApplication.class, args);
	}

}
