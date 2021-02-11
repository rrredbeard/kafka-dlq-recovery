package it.rrredbeard.kafka.dlq_recovery.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaCustomHeader {

	public static final String EVENT_TYPE = "x-event-type";
	public static final String EVENT_VERSION = "x-event-version";
	public static final String EVENT_RETRIES = "x-event-retries";

}
