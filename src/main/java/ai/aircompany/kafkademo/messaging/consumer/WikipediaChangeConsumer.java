package ai.aircompany.kafkademo.messaging.consumer;

import ai.aircompany.kafkademo.avro.WikipediaRecentChangeDataAvro;
import ai.aircompany.kafkademo.model.WikipediaRecentChangeData;
import ai.aircompany.kafkademo.repository.WikipediaRecentChangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class WikipediaChangeConsumer {

    private final WikipediaRecentChangeRepository wikipediaRecentChangeRepository;

    @KafkaListener(
            topics = "${app.kafka.topic:wikipedia-recentchange}",
            groupId = "wikipedia-change-consumer-group"
    )
    public void consume(ConsumerRecord<String, WikipediaRecentChangeDataAvro> record) {
        log.info("Registro consumido do Kafka: {}", record);

        var entity = toEntity(record.value());
        entity.setId(UUID.randomUUID().toString());

        wikipediaRecentChangeRepository.save(entity);

        log.info("Documento salvo no Elasticsearch com ID UUID: {} [Wiki: {}, Titulo: {}]",
                entity.getId(), entity.getWiki(), entity.getTitle());
    }

    private WikipediaRecentChangeData toEntity(WikipediaRecentChangeDataAvro value) {
        var entity = new WikipediaRecentChangeData();
        entity.setSchema(value.getSchema$());
        entity.setChangeId(value.getId());
        entity.setType(value.getType());
        entity.setNamespace(value.getNamespace());
        entity.setTitle(value.getTitle());
        entity.setTitleUrl(value.getTitleUrl());
        entity.setComment(value.getComment());
        entity.setParsedcomment(value.getParsedcomment());
        entity.setTimestamp(value.getTimestamp());
        entity.setUser(value.getUser());
        entity.setBot(value.getBot());
        entity.setNotifyUrl(value.getNotifyUrl());
        entity.setMinor(value.getMinor());
        entity.setPatrolled(value.getPatrolled());

        return entity;
    }
}
