package it.rrredbeard.kafka.dlq_recovery.model;

import lombok.*;
import org.springframework.lang.NonNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderDTO implements KEvent {

	public static final String ETYPE = "GenericOrder";

	private ProductType type;
	private Integer qty;
	private String id;

	@Override
	public String obtainEventKey() {
		return id;
	}

	@NonNull
	@Override
	public String obtainEventType() {
		return ETYPE;
	}

	@NonNull
	public static OrderDTO randomFactory() {
		return new OrderDTO(
			ProductType.values()[(int) (System.currentTimeMillis() % 3)],
			ThreadLocalRandom.current().nextInt(100, 999),
			UUID.randomUUID().toString()
		);
	}

	public enum ProductType {
		MONITOR,
		KEYBORD,
		MOUSE
	}
}
