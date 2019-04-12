package SemanticAnalysis.repository;

import SemanticAnalysis.model.FilmIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FilmIndexRepository extends ElasticsearchRepository<FilmIndex, Integer> {
}
