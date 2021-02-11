package it.rrredbeard.kafka.dlq_recovery.service;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;

public interface DLQService {

	void handle(@NonNull Message<?> message);

}
