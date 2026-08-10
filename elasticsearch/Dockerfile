FROM docker.elastic.co/elasticsearch/elasticsearch:8.12.2

USER root
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Ho_Chi_Minh
RUN apt-get update && apt-get install -y zip unzip wget cmake g++ openjdk-17-jdk && \
    wget -qO plugin.zip https://github.com/duydo/elasticsearch-analysis-vietnamese/releases/download/v8.7.0/elasticsearch-analysis-vietnamese-8.7.0.zip && \
    unzip plugin.zip -d plugin-ext && \
    sed -i 's/elasticsearch.version=8.7.0/elasticsearch.version=8.12.2/g' plugin-ext/plugin-descriptor.properties && \
    cd plugin-ext && zip -r ../plugin.zip * && cd .. && \
    wget -qO master.zip https://github.com/coccoc/coccoc-tokenizer/archive/master.zip && \
    unzip master.zip && \
    cd coccoc-tokenizer-master && \
    mkdir build && cd build && \
    cmake -DBUILD_JAVA=1 .. && \
    make install && \
    cp /usr/local/lib/libcoccoc_tokenizer* /usr/lib/ && \
    cd ../.. && rm -rf coccoc-tokenizer-master master.zip && \
    ldconfig

RUN bin/elasticsearch-plugin install -b file:///usr/share/elasticsearch/plugin.zip
USER elasticsearch
