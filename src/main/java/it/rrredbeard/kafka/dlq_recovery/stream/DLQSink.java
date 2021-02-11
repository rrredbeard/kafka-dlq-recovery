package it.rrredbeard.kafka.dlq_recovery.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DLQSink {

	String CHANNEL = "dlq";

	@Input(CHANNEL)
	SubscribableChannel dlq();

}
