package ai.aircompany.kafkademo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient wikipediaStreamWebClient(WebClient.Builder builder) {
        // Aumenta o tamanho máximo do ‘buffer’ na memória para evitar DataBufferLimitException
        var strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();

        return builder
                .exchangeStrategies(strategies)
                .baseUrl("https://stream.wikimedia.org/v2/stream/recentchange")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .build();
    }
}
