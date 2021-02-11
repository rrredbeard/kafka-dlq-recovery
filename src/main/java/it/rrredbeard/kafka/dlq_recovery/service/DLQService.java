package it.rrredbeard.kafka.dlq_recovery.service;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;

import java.util.Optional;

public interface DLQService {

	@NonNull
	Optional<Message<?>> handle(@NonNull Message<?> message); // NOSONAR

}
