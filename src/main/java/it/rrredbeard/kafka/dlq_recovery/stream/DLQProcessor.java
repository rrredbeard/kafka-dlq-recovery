package it.rrredbeard.kafka.dlq_recovery.stream;

import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;

public interface DLQProcessor extends Sink, Source {
}
