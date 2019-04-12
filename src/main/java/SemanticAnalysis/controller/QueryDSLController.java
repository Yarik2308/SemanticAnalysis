package SemanticAnalysis.controller;

import SemanticAnalysis.model.FilmIndex;
import SemanticAnalysis.service.QueryDSLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryDSLController {

    @Autowired
    private QueryDSLService service;

    @GetMapping("/search/{text}")
    public List<FilmIndex> search(@PathVariable String text){
        return service.search(text);
    }
}
