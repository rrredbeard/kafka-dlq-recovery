#!/usr/bin/env bash

export AUTO_CREATE_TOPICS="false"


export KAFKA_BROKERS=""

#export APP_NAME=

export TOPIC_IN=""
export TOPIC_PART_NO=30
export TOPIC_OUT=""

export LOG_DURATION_ENABLED="true"
export LOG_EXCEPTION_ENABLED="true"

# headers key separati da virgola, esempio "x-event-type, x-event-version"
export ALLOWED_HEADERS=""


cd $(dirname "$0") && \
  ./mvnw clean install spring-boot:run -DskipTests=true

exit 0
