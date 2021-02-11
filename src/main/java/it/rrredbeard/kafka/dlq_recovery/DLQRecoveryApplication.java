package it.rrredbeard.kafka.dlq_recovery;

import it.rrredbeard.kafka.dlq_recovery.config.AppConfig;
import it.rrredbeard.kafka.dlq_recovery.service.DLQService;
import it.rrredbeard.kafka.dlq_recovery.stream.DLQProcessor;
import it.rrredbeard.kafka.dlq_recovery.utils.BaseConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.cloud.stream.messaging.Sink.INPUT;
import static org.springframework.cloud.stream.messaging.Source.OUTPUT;

@Slf4j
@EnableAsync
@EnableTransactionManagement
@ConfigurationPropertiesScan(basePackages = "it.rrredbeard.kafka.dlq_recovery.config")
@SpringBootApplication
//
@Controller
@RequiredArgsConstructor
public class DLQRecoveryApplication extends BaseConsumer {

	public static void main(String[] args) {
		SpringApplication.run(DLQRecoveryApplication.class, args);
	}


	public static void logConfig(@NonNull Object conf) {
		log.debug("LOADED CONF | {}", conf);
	}

	/***********************/

	@Getter
	private final AppConfig config;
	private final DLQService dlqService;

	@Transactional
	@StreamListener(INPUT)
	@SendTo(OUTPUT)
	public Message<?> onEvent(@NonNull Message<?> message) { // NOSONAR

		final Optional<Message<?>> output;
		final long begin = startTrace(INPUT);

		output = dlqService.handle(message);

		endTrace(INPUT, begin);

		return output.orElse(null);
	}

}
