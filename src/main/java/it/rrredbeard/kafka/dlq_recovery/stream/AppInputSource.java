package it.rrredbeard.kafka.dlq_recovery.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AppInputSource {

	String CHANNEL = "input-producer";

	@Output(CHANNEL)
	MessageChannel inputChannel();

}
