package SemanticAnalysis;

import FilesHandlers.CommentsToCSV;
import FilesHandlers.XMLParser;
import SemanticAnalysis.connector.ElasticSearchConnector;
import WebCrawler.CommentsWeb;
import WebCrawler.Mappers;
import WekaAndStem.Comment;
import WekaAndStem.CommentStem;
import WekaAndStem.ScoreGetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//@SpringBootApplication
//@RestController
//public class SemanticAnalysisApplication {
//
//    public static void main(String[] args) {
//
//        ElasticSearchConnector es = null;
//
//        try {
//            es = new ElasticSearchConnector("SemanticAnalysis", "localhost", 9300);
//
//            // check if elastic search cluster is healthy
//            es.isClusterHealthy();
//
//            // check if index already existing
//            if (!es.isIndexRegistered("semantic_analysis_index")) {
//                System.out.println("Creating index");
//                // create index if not already existing
//                es.createIndex("semantic_analysis_index", 2, 0);
//            }
//            // check if index is empty
//            if(es.isIndexEmpty("semantic_analysis_index")){
//                System.out.println("Inser data");
//                // manually insert data
//                es.bulkInsert("semantic_analysis_index", "_doc");
//            }
//        }catch (UnknownHostException e){
//            e.printStackTrace();
//        }
//        es.close();
//
//        SpringApplication.run(SemanticAnalysisApplication.class, args);
//	}
//
//}



class Values{
    private Integer val1;
    private Integer val2;

    Values(Integer val1, Integer val2){
        this.val1 = val1;
        this.val2 = val2;
    }

    public void setVal1(Integer val1) { this.val1 = val1; }

    public void setVal2(Integer val2) { this.val2 = val2; }

    public Integer getVal1() { return val1; }

    public Integer getVal2() { return val2; }
}

public class SemanticAnalysisApplication {
	public static void main(String[] args) throws Exception{
		Mappers mappers = new Mappers();
        CommentStem stem = new CommentStem();


        ArrayList<Comment> comments1 = XMLParser.parse();
        ArrayList<CommentsWeb> comments2 = mappers.getAllComments();

        ArrayList<Comment> commentsWithout0 = new ArrayList<>();

        // Delete comments with unknown score
        for (Comment comment : comments1) {
            if (comment.getScore() != 0)
                commentsWithout0.add(comment);
        }
        System.out.println("ROMIP: " + commentsWithout0.size());
        System.out.println("Megacritic: " + comments2.size());
        int comm1size10 = commentsWithout0.size()/10;
        int comm2size10 = comments2.size()/10;

        //// Length of comments
        Map<Integer, Values> matrix = new TreeMap<>();

        int i = 0;
        for (Comment comment: commentsWithout0){
            if(!(i<comm1size10 || i>(commentsWithout0.size() - comm1size10))){
                if(matrix.containsKey(comment.getText().split(" ").length)){

                    Values values = matrix.get(comment.getText().split(" ").length);
                    values.setVal1(values.getVal1() + 1);

                    matrix.put(comment.getText().split(" ").length , values);
                } else {
                    Values values = new Values(1, 0);
                    matrix.put(comment.getText().split(" ").length, values);
                }
            }
            i++;
        }

        i = 0;
        for(CommentsWeb comment: comments2){
            if(!(i<comm2size10 || i>(comments2.size() - comm2size10))){
                if(matrix.containsKey(comment.getText().split(" ").length)){

                    Values values = matrix.get(comment.getText().split(" ").length);
                    values.setVal2(values.getVal2() + 1);
                    matrix.put(comment.getText().split(" ").length , values);
                } else {
                    Values values = new Values(0, 1);
                    matrix.put(comment.getText().split(" ").length, values);
                }
            }
            i++;
        }

        System.out.println(matrix.size());

//        System.out.println("========== Keys ============");
//        for(Map.Entry e: matrix.entrySet()){
//            System.out.println(e.getKey());
//        }
//        System.out.println("========== First Values ==========");
//        for(Map.Entry e: matrix.entrySet()){
//            Values values = matrix.get(e.getKey());
//            System.out.println(values.getVal1());
//        }
//        System.out.println("========== Second Values ==========");
//        for(Map.Entry e: matrix.entrySet()){
//            Values values = matrix.get(e.getKey());
//            System.out.println(values.getVal2());
//        }

        int proiz = 0;
        for(Map.Entry e: matrix.entrySet()){
            proiz = proiz + ((int) e.getKey())*(matrix.get(e.getKey()).getVal1());
        }
        System.out.println(proiz/commentsWithout0.size());

        proiz = 0;
        for(Map.Entry e: matrix.entrySet()){
            proiz = proiz + ((int) e.getKey())*(matrix.get(e.getKey()).getVal2());
        }
        System.out.println(proiz/comments2.size());
	}
}