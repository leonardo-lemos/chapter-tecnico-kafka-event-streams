package ai.aircompany.kafkademo.repository;


import ai.aircompany.kafkademo.model.WikipediaRecentChangeData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WikipediaRecentChangeRepository extends ElasticsearchRepository<WikipediaRecentChangeData, String> {
}
