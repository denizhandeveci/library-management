#!/usr/bin/env bash
set -e

if [ ! -f .env ]; then
	echo ".env file not found. Create one with: cp .env.template .env"
	exit 1
fi

set -a
source .env
set +a

./mvnw spring-boot:run