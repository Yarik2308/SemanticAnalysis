package FilesHandlers;

import WebCrawler.CommentsWeb;
import WebCrawler.Mappers;
import WekaAndStem.Comment;
import WekaAndStem.CommentStem;
import org.xml.sax.SAXException;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class FilesHandler {
    public static void main(String args[]) throws MyStemApplicationException, IOException,
            ParserConfigurationException, SAXException {

        // Create CSV file for Weka GUI.
        CommentStem Stemer = new CommentStem();
        String message;
//
//        ////// ROMIP comments
//        ArrayList<Comment> comments = XMLParser.parse();
//        ArrayList<Comment> commentsWithout0 = new ArrayList<>();
//        ArrayList<Comment> commentsWithoutShortComments = new ArrayList<>();
//
//        // Delete comments with unknown score
//        for (Comment comment : comments) {
//            if (comment.getScore() != 0 )
//                commentsWithout0.add(comment);
//        }
//
//        for (Comment comment : commentsWithout0) {
//            ArrayList<String> commentStemString = Stemer.Stem(comment.getText());
//            message = "";
//            for (String word : commentStemString) {
//                if (message.isEmpty())
//                    message = word;
//                else
//                    message = message + " " + word;
//            }
//            comment.setText(message);
//        }
//
//        for (Comment comment : commentsWithout0) {
//            if(comment.getText().length()>5){
//                commentsWithoutShortComments.add(comment);
//            }
//        }
//
//        CommentsToCSV.Convert3(commentsWithoutShortComments);

        ////// Megacritic Comments
        Mappers mappers = new Mappers();
        ArrayList<CommentsWeb> commentsWeb = mappers.getAllComments();

        int size[] = new int[11];
        for(int i = 0; i<11; i++){
            size[i]=0;
        }
        for(CommentsWeb comment: commentsWeb){
            size[comment.getScore()]++;
        }
        for(int i = 0; i<11; i++){
            System.out.println(i + " = "+ size[i]);
        }
//        ArrayList<CommentsWeb> commentsWithout0Web = new ArrayList<>();
//        ArrayList<CommentsWeb> commentsWithoutShortCommentsWeb = new ArrayList<>();
//
//        // Delete comments with unknown score
//        for (CommentsWeb comment : commentsWeb) {
//            if (comment.getScore() != 0 )
//                commentsWithout0Web.add(comment);
//        }
//
//        for (CommentsWeb comment : commentsWithout0Web) {
//            ArrayList<String> commentStemString = Stemer.Stem(comment.getText());
//            message = "";
//            for (String word : commentStemString) {
//                if (message.isEmpty())
//                    message = word;
//                else
//                    message = message + " " + word;
//                }
//            comment.setText(message);
//        }
//
//        for (CommentsWeb comment : commentsWithout0Web) {
//            if(comment.getText().length()>5){
//                commentsWithoutShortCommentsWeb.add(comment);
//            }
//        }
//
//        CommentsToCSV.Convert3Web3467(commentsWithoutShortCommentsWeb);
//        CommentsToCSV.Convert3Web3478(commentsWithoutShortCommentsWeb);
//        CommentsToCSV.Convert3Web4567(commentsWithoutShortCommentsWeb);
//        CommentsToCSV.Convert3Web6789(commentsWithoutShortCommentsWeb);

    }
}
