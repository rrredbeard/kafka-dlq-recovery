package it.rrredbeard.kafka.dlq_recovery.service;

import it.rrredbeard.kafka.dlq_recovery.model.OrderDTO;
import org.springframework.lang.NonNull;

public interface OrderService {

	void handle(@NonNull OrderDTO order);

}
