package it.rrredbeard.kafka.dlq_recovery.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageHeaders;

import static it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader.EVENT_TYPE;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION_ID;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseConsumer {

	@SuppressWarnings("unused")
	public abstract void consumeVoid(@NonNull MessageHeaders headers);


	protected static void logIncomingEvent(@NonNull MessageHeaders headers) {

		if (log.isDebugEnabled()) {
			log.debug("TRACE EVENT --{}-- FROM [{}-{}]",
				headers.get(EVENT_TYPE),
				headers.get(RECEIVED_TOPIC),
				headers.get(RECEIVED_PARTITION_ID)
			);
		}
	}

	protected static long startTrace(@NonNull String target) {

		if (log.isTraceEnabled()) {
			log.trace("TRACE BEGIN --{}--", target);
		}

		return System.currentTimeMillis();
	}

	protected static void endTrace(@NonNull String target, final long begin) {

		if (log.isTraceEnabled()) {
			final long duration = System.currentTimeMillis() - begin;

			log.debug("TRACE END --{}-- [duration = {}ms]", target, duration);
		}
	}
}
