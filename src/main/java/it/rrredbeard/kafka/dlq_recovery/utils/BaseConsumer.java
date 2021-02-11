package it.rrredbeard.kafka.dlq_recovery.utils;

import it.rrredbeard.kafka.dlq_recovery.config.AppConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseConsumer {

	public abstract AppConfig getConfig();


	protected long startTrace(@NonNull String target) {

		if (log.isTraceEnabled()) {
			log.trace("TRACE BEGIN --{}--", target);
		}

		return System.currentTimeMillis();
	}

	protected void endTrace(@NonNull String target, final long begin) {

		if (getConfig().isLogDurationEnabled() || log.isTraceEnabled()) {
			final long duration = System.currentTimeMillis() - begin;

			log.debug("TRACE END --{}-- [duration = {}ms]", target, duration);
		}
	}
}
