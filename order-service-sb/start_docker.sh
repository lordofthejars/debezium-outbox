#!/bin/bash

docker run -ti --rm -p 5433:5432 --env-file .env quay.io/debezium/example-postgres:1.8