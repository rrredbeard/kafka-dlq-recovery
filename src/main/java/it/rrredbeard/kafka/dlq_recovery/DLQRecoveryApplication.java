package it.rrredbeard.kafka.dlq_recovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class DLQRecoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DLQRecoveryApplication.class, args);
	}

}
