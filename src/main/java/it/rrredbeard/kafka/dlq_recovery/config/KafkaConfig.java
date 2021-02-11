package it.rrredbeard.kafka.dlq_recovery.config;

import it.rrredbeard.kafka.dlq_recovery.DLQRecoveryApplication;
import it.rrredbeard.kafka.dlq_recovery.stream.DLQProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binder.kafka.KafkaBindingRebalanceListener;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.cloud.stream.binder.kafka.utils.DlqPartitionFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;

@Slf4j
@Configuration
@EnableBinding({DLQProcessor.class})
public class KafkaConfig {

	private static final String TX_ID_PREFIX_PROP = "spring.cloud.stream.kafka.binder.transaction.transactionIdPrefix";


	@Bean
	public KafkaBindingRebalanceListener kafkaBindingRebalanceListener() {
		return new SDHKafkaBindingRebalanceListener();
	}

	@Bean
	public DlqPartitionFunction partitionFunction() {
		return (group, record, ex) -> 0;
	}

	@Bean
	@ConditionalOnProperty(TX_ID_PREFIX_PROP)
	@SuppressWarnings("ConstantConditions")
	public PlatformTransactionManager kafkaTransactionManager(
		@NonNull @Value("${" + TX_ID_PREFIX_PROP + "}") String txIdPrefix,
		@NonNull BinderFactory binders
	) {
		final KafkaTransactionManager<byte[], byte[]> transactionManager;

		final String confName = null;
		final String newTxIdPrefix = format("%d-%s", rand8d(), txIdPrefix);

		final KafkaMessageChannelBinder messageChannelBinder =
			(KafkaMessageChannelBinder) binders.getBinder(confName, MessageChannel.class);

		transactionManager = new KafkaTransactionManager<>(
			messageChannelBinder.getTransactionalProducerFactory()
		);

		transactionManager.setTransactionIdPrefix(newTxIdPrefix);

		DLQRecoveryApplication.logConfig(
			format("KafkaTransactionManager(txIdPrefix=%s)", newTxIdPrefix)
		);

		return transactionManager;
	}

	private static long rand8d() {
		return ThreadLocalRandom.current().nextLong(10000000, 99999999);
	}

	private static final class SDHKafkaBindingRebalanceListener implements KafkaBindingRebalanceListener {

		@Override
		public void onPartitionsAssigned(
			String bindingName,
			org.apache.kafka.clients.consumer.Consumer<?, ?> consumer,
			Collection<TopicPartition> partitions,
			boolean initial
		) {
			log.info("Partition assigned = {} ", partitions);
		}
	}

}
