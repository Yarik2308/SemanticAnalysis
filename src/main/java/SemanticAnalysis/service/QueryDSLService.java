package SemanticAnalysis.service;

import SemanticAnalysis.model.FilmIndex;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryDSLService {

    @Autowired
    private ElasticsearchTemplate template;

    public List<FilmIndex> search(String text){
        String search = ".*" + text + ".*";
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(search)
        .field("name").field("description").field("genres").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();

        List<FilmIndex> films = template.queryForList(searchQuery, FilmIndex.class);
        return films;
    }
}
