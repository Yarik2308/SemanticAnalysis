package SemanticAnalysis;

import SemanticAnalysis.connector.ElasticSearchConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class SemanticAnalysisApplication {

    public static void main(String[] args) {

        ElasticSearchConnector es = null;

        try {
            es = new ElasticSearchConnector("SemanticAnalysis", "localhost", 9300);

            // check if elastic search cluster is healthy
            es.isClusterHealthy();

            // check if index already existing
            if (!es.isIndexRegistered("semantic_analysis_index")) {
                System.out.println("Creating index");
                // create index if not already existing
                es.createIndex("semantic_analysis_index", 2, 0);
            }
            // check if index is empty
            if(es.isIndexEmpty("semantic_analysis_index")){
                System.out.println("Inser data");
                // manually insert data
                es.bulkInsert("semantic_analysis_index", "_doc");
            }
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        es.close();

        SpringApplication.run(SemanticAnalysisApplication.class, args);
	}

}



//class Values{
//    private Integer val1;
//    private Integer val2;
//
//    Values(Integer val1, Integer val2){
//        this.val1 = val1;
//        this.val2 = val2;
//    }
//
//    public void setVal1(Integer val1) { this.val1 = val1; }
//
//    public void setVal2(Integer val2) { this.val2 = val2; }
//
//    public Integer getVal1() { return val1; }
//
//    public Integer getVal2() { return val2; }
//}

//public class SemanticAnalysisApplication {
//	public static void main(String[] args) throws Exception{
//		Mappers mappers = new Mappers();
//        CommentStem stem = new CommentStem();


//        ArrayList<Comment> comments1 = XMLParser.parse();
//        ArrayList<CommentsWeb> comments2 = mappers.getAllComments();
//
//        ArrayList<Comment> commentsWithout0 = new ArrayList<>();
//
//        // Delete comments with unknown score
//        for (Comment comment : comments1) {
//            if (comment.getScore() != 0)
//                commentsWithout0.add(comment);
//        }
//        System.out.println("ROMIP: " + commentsWithout0.size());
//        System.out.println("Megacritic: " + comments2.size());

        ////// Length of comments
//        Map<Integer, Values> matrix = new TreeMap<>();
//
//        for (Comment comment: commentsWithout0){
//            if(matrix.containsKey(comment.getText().split(" ").length)){
//
//                Values values = matrix.get(comment.getText().split(" ").length);
//                values.setVal1(values.getVal1() + 1);
//
//                matrix.put(comment.getText().split(" ").length , values);
//            } else {
//                Values values = new Values(1, 0);
//                matrix.put(comment.getText().split(" ").length, values);
//            }
//        }
//
//        for(CommentsWeb comment: comments2){
//            if(matrix.containsKey(comment.getText().split(" ").length)){
//
//                Values values = matrix.get(comment.getText().split(" ").length);
//                values.setVal2(values.getVal2() + 1);
//                matrix.put(comment.getText().split(" ").length , values);
//            } else {
//                Values values = new Values(0, 1);
//                matrix.put(comment.getText().split(" ").length, values);
//            }
//        }
//
//        System.out.println(matrix.size());

//        System.out.println("========== Keys ============");
//        for(Map.Entry i: matrix.entrySet()){
//            System.out.println(i.getKey());
//        }
//        System.out.println("========== First Values ==========");
//        for(Map.Entry i: matrix.entrySet()){
//            Values values = matrix.get(i.getKey());
//            System.out.println(values.getVal1());
//        }
//        System.out.println("========== Second Values ==========");
//        for(Map.Entry i: matrix.entrySet()){
//            Values values = matrix.get(i.getKey());
//            System.out.println(values.getVal2());
//        }
//		for(CommentsWeb comment: comments){
//            ArrayList<String> commentStemString = stem.Stem(comment.getText());
//            String stemText = "";
//            for (String word : commentStemString) {
//                if (stemText.isEmpty())
//                    stemText = word;
//                else
//                    stemText = stemText + " " + word;
//            }
//		    comment.setText(stemText);
//        }
//
//		CommentsToCSV.Convert3Web(comments);


		/////// ConfusionMatrix and Accuracity
//		int match = 0;
//		int[][] Rez = {
//				{ 0, 0, 0 },
//				{ 0, 0, 0 },
//				{ 0, 0, 0 }
//		};
//
//		ScoreGetter getter = new ScoreGetter();
//
//		for(CommentsWeb comment: comments){
//			String result = getter.predict(comment.getText());
//			if(result == "good"){
//				comment.setScoreMLT(3);
//			} else if(result == "normal") {
//				comment.setScoreMLT(2);
//			} else{
//				comment.setScoreMLT(1);
//			}
//			if(comment.getScore()<4){
//				if(comment.getScoreMLT() == 1){
//					Rez[0][0]++;
//					match++;
//				}else if(comment.getScoreMLT() == 2){
//					Rez[0][1]++;
//				}else{
//					Rez[0][2]++;
//				}
//			} else if(comment.getScore()<7){
//				if(comment.getScoreMLT() == 1){
//					Rez[1][0]++;
//				}else if(comment.getScoreMLT() == 2){
//					Rez[1][1]++;
//					match++;
//				}else{
//					Rez[1][2]++;
//				}
//			} else{
//				if(comment.getScoreMLT() == 1){
//					Rez[2][0]++;
//				}else if(comment.getScoreMLT() == 2){
//					Rez[2][1]++;
//				}else{
//					Rez[2][2]++;
//					match++;
//				}
//			}
//		}

//		System.out.println("Comments size: " + comments.size());
//		System.out.println("Correctly Classified: " + (match * 100.0f)  / comments.size());
//		System.out.println(Rez[0][0] + "  " + Rez[0][1] + "  " + Rez[0][2]);
//		System.out.println(Rez[1][0] + "  " + Rez[1][1] + "  " + Rez[1][2]);
//		System.out.println(Rez[2][0] + "  " + Rez[2][1] + "  " + Rez[2][2]);
//
//	}
//}