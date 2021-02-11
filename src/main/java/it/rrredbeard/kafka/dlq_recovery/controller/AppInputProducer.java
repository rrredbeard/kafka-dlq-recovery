package it.rrredbeard.kafka.dlq_recovery.controller;

import it.rrredbeard.kafka.dlq_recovery.model.OrderDTO;
import it.rrredbeard.kafka.dlq_recovery.service.KEventSender;
import it.rrredbeard.kafka.dlq_recovery.stream.AppInputSource;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public final class AppInputProducer {

	private final AppInputSource appInputSource;
	private final KEventSender kEventSender;

	@Scheduled(fixedRate = 15000, initialDelay = 5000)
	void sendInput() {

		kEventSender.send(
			appInputSource.inputChannel(),
			OrderDTO.randomFactory()
		);

	}
}
