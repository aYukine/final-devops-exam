FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    openssh-server \
    git \
    nginx \
    curl \
    openjdk-21-jdk \
    php-cli \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir /var/run/sshd
RUN echo 'root:Hello@123' | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's/#Port 22/Port 2222/' /etc/ssh/sshd_config

WORKDIR /app

# Clone your project's Ex1 branch directly inside the container environment
RUN git clone -b Ex1 https://github.com/aYukine/final-devops-exam.git .

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8443 2222

CMD service ssh start && nginx && ./gradlew bootRun