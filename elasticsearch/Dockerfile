FROM docker.elastic.co/elasticsearch/elasticsearch:8.12.2

USER root
RUN apt-get update && apt-get install -y zip unzip wget && \
    wget -qO plugin.zip https://github.com/duydo/elasticsearch-analysis-vietnamese/releases/download/v8.7.0/elasticsearch-analysis-vietnamese-8.7.0.zip && \
    unzip plugin.zip -d plugin-ext && \
    sed -i 's/elasticsearch.version=8.7.0/elasticsearch.version=8.12.2/g' plugin-ext/plugin-descriptor.properties && \
    cd plugin-ext && zip -r ../plugin.zip * && cd .. && \
    rm -rf plugin-ext && \
    chown elasticsearch:elasticsearch plugin.zip

USER elasticsearch
RUN bin/elasticsearch-plugin install -b file:///usr/share/elasticsearch/plugin.zip
