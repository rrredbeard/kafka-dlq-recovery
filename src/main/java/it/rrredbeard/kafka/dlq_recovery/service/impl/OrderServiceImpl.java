package it.rrredbeard.kafka.dlq_recovery.service.impl;

import it.rrredbeard.kafka.dlq_recovery.model.OrderDTO;
import it.rrredbeard.kafka.dlq_recovery.model.error.ExcessiveAmountException;
import it.rrredbeard.kafka.dlq_recovery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	@Override
	public void handle(@NonNull OrderDTO order) {
		log.info("HANDLE -- {}", order);

		if(order.getQty() % 2 == 0){
			throw new ExcessiveAmountException();
		}
	}
}
