package it.rrredbeard.kafka.dlq_recovery.service.impl;

import it.rrredbeard.kafka.dlq_recovery.service.DLQService;
import it.rrredbeard.kafka.dlq_recovery.stream.AppInputSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader.EVENT_RETRIES;
import static it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader.EVENT_TYPE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DLQServiceImpl implements DLQService {

	private final AppInputSource inputSource;

	@Override
	public void handle(@NonNull Message<?> failed) {

		final MessageHeaders headers = failed.getHeaders();
		final Integer retries = Optional.ofNullable(
			headers.get(EVENT_RETRIES, Integer.class)
		).orElse(0);

		log.info("HANDLE DLQ -- [type = {}, retryAttempt = {}, exception = {}] | {}",
			headers.get(EVENT_TYPE),
			retries,
			headers.get(KafkaHeaders.REPLY_TOPIC),
			failed.getPayload()
		);


		if (retries >= 3) {
			log.info("Retries exhausted for: {}@[topic = {}, partition = {}, offset = {}]",
				headers.get(EVENT_TYPE),
				headers.get(KafkaHeaders.RECEIVED_TOPIC),
				headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
				headers.get(KafkaHeaders.OFFSET)
			);

			return;
		}


		final Message<?> message;
		message = MessageBuilder.withPayload(failed.getPayload())
					  .setHeader(EVENT_RETRIES, retries + 1)
					  .setHeader(
						  BinderHeaders.PARTITION_OVERRIDE,
						  headers.get(KafkaHeaders.RECEIVED_PARTITION_ID)
					  )
					  .setHeader(
						  EVENT_TYPE,
						  headers.get(EVENT_TYPE)
					  )
					  .setHeader(
						  KafkaHeaders.MESSAGE_KEY,
						  headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY)
					  )
					  .build();

		inputSource.inputChannel().send(message);

	}
}
