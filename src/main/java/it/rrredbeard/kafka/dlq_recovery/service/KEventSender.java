package it.rrredbeard.kafka.dlq_recovery.service;

import it.rrredbeard.kafka.dlq_recovery.model.KEvent;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageChannel;

public interface KEventSender {

	<E extends KEvent> void send(@NonNull MessageChannel channel, @NonNull E event);

}
