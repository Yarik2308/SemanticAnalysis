package SemanticAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "genres")
public class Genre {
    @Id
    @Column(name = "film_id")
    Integer film_id;
    @Column(name = "genre")
    String genre;
}
