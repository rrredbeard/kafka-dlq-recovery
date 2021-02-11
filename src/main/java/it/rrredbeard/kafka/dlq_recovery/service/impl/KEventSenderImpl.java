package it.rrredbeard.kafka.dlq_recovery.service.impl;

import it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader;
import it.rrredbeard.kafka.dlq_recovery.model.KEvent;
import it.rrredbeard.kafka.dlq_recovery.service.KEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class KEventSenderImpl implements KEventSender {

	@Override
	@Transactional
	public <E extends KEvent> void send(@NonNull MessageChannel channel, @NonNull E event) {

		final boolean messageSentCorrectly =
			channel.send(
				messageFrom(event)
			);

		if (!messageSentCorrectly) {
			throw new MessageDeliveryException("Submit failed");
		}
	}

	@NonNull
	private Message<?> messageFrom(@NonNull KEvent event) {

		return MessageBuilder.withPayload(event)
				   .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
				   .setHeader(KafkaCustomHeader.EVENT_TYPE, event.obtainEventType())
				   .setHeader(KafkaCustomHeader.EVENT_VERSION, event.obtainEventVersion())
				   .setHeader(KafkaHeaders.MESSAGE_KEY, event.obtainEventKey())
				   .build();
	}

}
