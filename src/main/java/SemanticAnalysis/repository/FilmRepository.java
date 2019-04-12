package SemanticAnalysis.repository;


import SemanticAnalysis.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer> {
}
