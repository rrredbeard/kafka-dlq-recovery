#!/usr/bin/env bash

export AUTO_CREATE_TOPICS="false"

# hostname e porta dei broker kafka
# esempio "kafka1:9092, kafka2:9092, kafka3:9092"
export KAFKA_BROKERS=""

# Setta il nome dell' app
# decommenta per settare un valore fisso come consumer-group
# default: random (cambia ad ogni avvio)
#export APP_NAME=dlq-recv-app
#export APP_LOG_LEVEL=info


# Nome del topic da consumare
export TOPIC_IN=""
# Totale delle partizioni del Topic da consumare
export TOPIC_PART_NO=30
# Nome del topic su cui produrre i messaggi
export TOPIC_OUT=""


# Stampa la durata dell' elaborazione per ogni messaggio
export LOG_DURATION_ENABLED="false"

# In fase di elaborazione, se esiste, stampa il nome dell' eccezione
# che presente tra i metadati del messaggio
export LOG_EXCEPTION_ENABLED="false"

# Proprietà dei metadati da aggiungere in fase di produzione del messaggio.
# esempio "x-event-type, x-event-version"
export ALLOWED_HEADERS=""


cd $(dirname "$0") && \
  ./mvnw clean package spring-boot:run  --quiet -DskipTests=true

exit 0
