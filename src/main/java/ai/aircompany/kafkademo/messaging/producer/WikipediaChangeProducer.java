package ai.aircompany.kafkademo.messaging.producer;

import ai.aircompany.kafkademo.avro.WikipediaRecentChangeDataAvro;
import ai.aircompany.kafkademo.model.dto.WikipediaRecentChangeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class WikipediaChangeProducer {

    private final WebClient webClient;

    private final KafkaTemplate<String, WikipediaRecentChangeDataAvro> kafkaTemplate;

    @Value("${app.kafka.topic:wikipedia-recentchange}")
    private String topicName;

    public WikipediaChangeProducer(@Qualifier("wikipediaStreamWebClient") WebClient webClient, KafkaTemplate<String, WikipediaRecentChangeDataAvro> kafkaTemplate) {
        this.webClient = webClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        log.info("Iniciando ingestão de dados da Wikipedia...");

        var typeRef = new ParameterizedTypeReference<ServerSentEvent<WikipediaRecentChangeDTO>>() {};
        var retrySpec = Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2))
                .maxBackoff(Duration.ofMinutes(1))
                .doBeforeRetry(retry ->
                        log.warn("Erro no stream da Wikipedia. Tentando reconectar... Erro: {}", retry.failure().getMessage())
                );

        webClient.get()
                .retrieve()
                .bodyToFlux(typeRef)
                .mapNotNull(ServerSentEvent::data)
                .map(this::convertToAvro)
                .retryWhen(retrySpec)
                .subscribe(
                        this::sendToKafka,
                        error -> log.error("Erro ao processar o stream da Wikipedia: {}", error.getMessage(), error),
                        () -> log.warn("Stream da Wikipedia finalizado.")
        );
    }

    private void sendToKafka(WikipediaRecentChangeDataAvro record) {
        var key = record.getId() != null ? record.getId().toString() : null;

        kafkaTemplate.send(topicName, key, record)
                .whenComplete((sendResult, throwable) -> {
                    if (throwable != null) {
                        log.error("Erro ao enviar registro para o Kafka: {}", throwable.getMessage(), throwable);
                    } else {
                        log.info("Registro enviado para o Kafka com sucesso. Tópico: {}, Partição: {}, Offset: {}",
                                sendResult.getRecordMetadata().topic(),
                                sendResult.getRecordMetadata().partition(),
                                sendResult.getRecordMetadata().offset());
                    }
                });
    }

    private WikipediaRecentChangeDataAvro convertToAvro(WikipediaRecentChangeDTO wikipediaRecentChangeData) {
        return WikipediaRecentChangeDataAvro.newBuilder()
                .setSchema$(wikipediaRecentChangeData.getSchema())
                .setId(wikipediaRecentChangeData.getId())
                .setType(wikipediaRecentChangeData.getType())
                .setNamespace(wikipediaRecentChangeData.getNamespace())
                .setTitle(wikipediaRecentChangeData.getTitle())
                .setTitleUrl(wikipediaRecentChangeData.getTitleUrl())
                .setComment(wikipediaRecentChangeData.getComment())
                .setParsedcomment(wikipediaRecentChangeData.getParsedcomment())
                .setTimestamp(wikipediaRecentChangeData.getTimestamp())
                .setUser(wikipediaRecentChangeData.getUser())
                .setBot(wikipediaRecentChangeData.getBot())
                .setNotifyUrl(wikipediaRecentChangeData.getNotifyUrl())
                .setMinor(wikipediaRecentChangeData.getMinor())
                .setPatrolled(wikipediaRecentChangeData.getPatrolled())
                .build();
    }


}
