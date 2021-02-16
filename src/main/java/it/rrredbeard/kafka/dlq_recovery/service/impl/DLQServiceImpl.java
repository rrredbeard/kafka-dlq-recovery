package it.rrredbeard.kafka.dlq_recovery.service.impl;

import it.rrredbeard.kafka.dlq_recovery.config.AppConfig;
import it.rrredbeard.kafka.dlq_recovery.service.DLQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DLQServiceImpl implements DLQService {

	private final AppConfig config;

	@NonNull
	@Override
	@Transactional
	public Optional<Message<?>> handle(@NonNull Message<?> inbound) {

		Message<?> outbound = null;

		final MessageBuilder<?> builder;
		final MessageHeaders headers = inbound.getHeaders();
		final boolean noRetryEntryExists = config.noRetryEntryExists();

		if (config.isLogDlqExceptionEnabled() || log.isTraceEnabled()) {
			Optional
				.ofNullable(headers.get(KafkaMessageChannelBinder.X_EXCEPTION_FQCN))
				.filter(byte[].class::isInstance)
				.ifPresent(v -> log.debug(
					"HANDLE DLQ EXCEPTION {}",
					new String((byte[]) v, StandardCharsets.UTF_8)
				));
		}

		if (noRetryEntryExists) {
			final boolean existsNoRetryMarker =
				Objects.equals(
					config.getNoRetryValue(),
					headers.get(config.getNoRetryHeader())
				);

			if (existsNoRetryMarker) {
				log.warn("EVENT ALREADY TRIED [topic = {}, partition = {}, offset = {}]",
					headers.get(KafkaHeaders.RECEIVED_TOPIC),
					headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
					headers.get(KafkaHeaders.OFFSET)
				);

				return Optional.empty();
			}
		}

		try {
			builder = MessageBuilder.withPayload(inbound.getPayload())
						  .setHeader(
							  BinderHeaders.PARTITION_OVERRIDE,
							  headers.get(KafkaHeaders.RECEIVED_PARTITION_ID)
						  )
						  .setHeader(
							  KafkaHeaders.MESSAGE_KEY,
							  headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY)
						  );

			if (noRetryEntryExists) {
				builder.setHeader(config.getNoRetryHeader(), config.getNoRetryValue());
			}

			for (String customHeader : config.getAllowedHeaders()) {
				builder.setHeader(
					customHeader,
					headers.get(customHeader)
				);
			}

			outbound = builder.build();

		} catch (RuntimeException ex) {
			log.error("FATAL ERROR ON [topic = {}, partition = {}, offset = {}]",
				headers.get(KafkaHeaders.RECEIVED_TOPIC),
				headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
				headers.get(KafkaHeaders.OFFSET),
				ex
			);

		}

		if (outbound != null && log.isInfoEnabled()) {
			log.info("HANDLE FROM [{}-{}] | {}",
				headers.get(KafkaHeaders.RECEIVED_TOPIC),
				headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
				outbound.getHeaders()
			);
		}

		return Optional.ofNullable(outbound);
	}
}
