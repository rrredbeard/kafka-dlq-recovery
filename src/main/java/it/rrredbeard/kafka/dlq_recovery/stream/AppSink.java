package it.rrredbeard.kafka.dlq_recovery.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AppSink {

	String CHANNEL = "input";

	@Input(CHANNEL)
	SubscribableChannel input();

}
