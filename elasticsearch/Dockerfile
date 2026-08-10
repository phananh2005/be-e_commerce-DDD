FROM almalinux:9 AS cxx-builder
RUN dnf install -y git cmake gcc-c++ java-21-openjdk-devel which
RUN git clone https://github.com/duydo/coccoc-tokenizer.git && \
    cd coccoc-tokenizer && mkdir build && cd build && cmake -DBUILD_JAVA=1 .. && make install

FROM maven:3.9-eclipse-temurin-22 AS java-builder
RUN git clone --branch v9.3.0 https://github.com/duydo/elasticsearch-analysis-vietnamese.git && \
    cd elasticsearch-analysis-vietnamese && mvn clean package -DskipTests

FROM docker.elastic.co/elasticsearch/elasticsearch:9.3.0
USER root
COPY --from=cxx-builder /usr/local/lib/libcoccoc_tokenizer* /usr/lib/
COPY --from=cxx-builder /usr/local/share/tokenizer /usr/local/share/tokenizer
RUN ldconfig
COPY --from=java-builder /elasticsearch-analysis-vietnamese/target/releases/elasticsearch-analysis-vietnamese-9.3.0.zip /tmp/plugin.zip
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install -b file:///tmp/plugin.zip && rm /tmp/plugin.zip
USER elasticsearch
