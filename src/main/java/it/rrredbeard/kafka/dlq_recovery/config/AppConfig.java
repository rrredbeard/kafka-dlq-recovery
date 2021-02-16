package it.rrredbeard.kafka.dlq_recovery.config;

import it.rrredbeard.kafka.dlq_recovery.DLQRecoveryApplication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

import static net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils.isNoneBlank;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "dlq-recovery-app")
public class AppConfig {

	@PostConstruct
	public final void logMe() {
		//noinspection ResultOfMethodCallIgnored
		getAllowedHeaders();

		DLQRecoveryApplication.logConfig(this);
	}


	private boolean logDurationEnabled = false;
	private boolean logDlqExceptionEnabled = false;

	private int allowedHeadersSize;
	private Set<String> allowedHeaders = new HashSet<>();

	private String noRetryHeader;
	private String noRetryValue;

	public final int getAllowedHeadersSize() {
		return (allowedHeadersSize = allowedHeaders.size()); // NOSONAR
	}

	public final boolean noRetryEntryExists(){
		return isNoneBlank(noRetryHeader, noRetryValue);
	}

}
