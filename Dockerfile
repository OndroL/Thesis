FROM quay.io/wildfly/wildfly:35.0.0.Final-jdk21

USER root
RUN microdnf install -y nano && microdnf clean all
