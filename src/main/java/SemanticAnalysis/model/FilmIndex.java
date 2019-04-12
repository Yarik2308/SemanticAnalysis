package SemanticAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "semantic_analysis_index", type = "_doc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmIndex {

    @Id
    Integer id;
    String name, description;
    List<String> genres;

}
