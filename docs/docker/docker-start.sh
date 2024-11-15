#!/bin/bash

cp .env.example .env

docker-compose -p llmchat -f ./docker-compose.yml up -d
