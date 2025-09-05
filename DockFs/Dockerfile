FROM jenkins/inbound-agent:latest

USER root

# Install Docker
RUN apt-get update && apt-get install -y docker.io && apt-get install -y skopeo

# Copying script into the image
COPY mds-docker.sh /opt/mds-docker.sh

# Remove default 'docker' group and re-create it with GID 1001 to match the host
RUN groupdel docker && \
    groupadd -g 1001 docker && \
    usermod -aG docker jenkins

RUN mkdir -p /run/containers && chmod 777 /run/containers

USER jenkins

CMD ["/opt/mds-docker.sh"]

