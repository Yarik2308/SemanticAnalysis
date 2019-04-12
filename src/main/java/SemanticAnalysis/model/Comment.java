package SemanticAnalysis.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "comments")
public class Comment {

    @Id
    @Column(name = "comment_id")
    private Integer id;
    @Column(name = "film_id")
    private Integer filmId;
    @Column(name = "score")
    private Integer score;
    @Column(name = "score_mlt")
    private Integer scoreMLT;
    @Column(name = "text")
    private String text;
    @Column(name = "author")
    private String author;
}
