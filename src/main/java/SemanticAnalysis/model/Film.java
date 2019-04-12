package SemanticAnalysis.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Id;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "films")
public class Film {

    @Id
    @Column(name = "film_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "img_link")
    private String imgLink;
    @Column(name = "viewers_score")
    private double viewersScore;
    @Column(name = "critics_score")
    private double criticsScore;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private List<Comment> comments;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private List<Genre> genres;
}
