package it.rrredbeard.kafka.dlq_recovery.controller;

import it.rrredbeard.kafka.dlq_recovery.model.OrderDTO;
import it.rrredbeard.kafka.dlq_recovery.service.OrderService;
import it.rrredbeard.kafka.dlq_recovery.stream.AppSink;
import it.rrredbeard.kafka.dlq_recovery.utils.BaseConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static it.rrredbeard.kafka.dlq_recovery.config.KafkaCustomHeader.EVENT_TYPE;


@Controller
@RequiredArgsConstructor
public final class AppConsumer extends BaseConsumer {

	private final OrderService orderService;

	@StreamListener(
		target = AppSink.CHANNEL,
		condition = "headers['" + EVENT_TYPE + "'] == '" + OrderDTO.ETYPE + "' "
	)
	public void consume(@Payload OrderDTO event) {
		final long start = startTrace(AppSink.CHANNEL);

		orderService.handle(event);

		endTrace(AppSink.CHANNEL, start);
	}

	@Override
	@StreamListener(target = AppSink.CHANNEL)
	public void consumeVoid(@NonNull @Headers MessageHeaders headers) {
		logIncomingEvent(headers);
	}

}
