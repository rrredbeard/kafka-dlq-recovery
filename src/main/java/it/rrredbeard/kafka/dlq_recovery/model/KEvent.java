package it.rrredbeard.kafka.dlq_recovery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static java.lang.String.format;

public interface KEvent {

	@Nullable
	@JsonIgnore
	default String obtainEventKey(){
		return null;
	}

	@NonNull
	@JsonIgnore
	default String obtainEventType(){
		return getClass().getSimpleName();
	}

	@NonNull
	@JsonIgnore
	default String obtainEventVersion(){
		return "v0.0.0";
	}

	@NonNull
	@JsonProperty("__signature__")
	default String obtainEventSignature(){
		return format("%s/%s", obtainEventType(), obtainEventVersion());
	}
}
