# syntax=docker/dockerfile:1

FROM ubuntu:latest
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get -y install openjdk-11-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
# FROM openjdk:17-alpine
COPY script.sh /workspace/
WORKDIR /workspace/
# RUN ["javac", "$1.class"]
# ENTRYPOINT ["/bin/sh", "script.sh", "$@"]
# ENTRYPOINT ["/bin/sh"]
