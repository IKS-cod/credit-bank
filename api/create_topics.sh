#!/bin/bash
set -e

TOPICS=(
  finish-registration
  create-documents
  send-documents
  send-ses
  credit-issued
  statement-denied
)

# Ждем, пока Kafka станет доступен
until kafka-topics.sh --bootstrap-server $KAFKA_BROKER_CONNECT --list; do
  echo "Waiting for Kafka..."
  sleep 5
done

for topic in "${TOPICS[@]}"; do
  kafka-topics.sh --bootstrap-server $KAFKA_BROKER_CONNECT --create --topic "$topic" --partitions 1 --replication-factor 1 || true
done

echo "All topics created or already exist."
