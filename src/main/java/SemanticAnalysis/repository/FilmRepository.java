package SemanticAnalysis.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import SemanticAnalysis.model.Film;

import java.util.List;

public interface FilmRepository extends ElasticsearchRepository<Film, Integer> {

}
