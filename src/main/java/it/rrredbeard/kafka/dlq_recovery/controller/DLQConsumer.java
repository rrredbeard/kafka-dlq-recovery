package it.rrredbeard.kafka.dlq_recovery.controller;

import it.rrredbeard.kafka.dlq_recovery.service.DLQService;
import it.rrredbeard.kafka.dlq_recovery.stream.DLQSink;
import it.rrredbeard.kafka.dlq_recovery.utils.BaseConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Controller;

import static it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader.EVENT_TYPE;
import static java.lang.String.format;

@Controller
@RequiredArgsConstructor
public final class DLQConsumer extends BaseConsumer {

	private final DLQService dlqService;

	@StreamListener(target = DLQSink.CHANNEL)
	public void consume(Message<?> message, @Header(EVENT_TYPE) String eType) {

		final String target = format("%s->%s", DLQSink.CHANNEL, eType);
		final long start = startTrace(target);

		dlqService.handle(message);

		endTrace(target, start);
	}

	@Override
	@StreamListener(target = DLQSink.CHANNEL)
	public void consumeVoid(@NonNull @Headers MessageHeaders headers) {
		logIncomingEvent(headers);
	}
}
