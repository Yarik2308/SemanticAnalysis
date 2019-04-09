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
        String message;
        CommentStem Stemer = new CommentStem();
        Mappers mappers = new Mappers();
        //ArrayList<Comment> comments = XMLParser.parse();
        ArrayList<CommentsWeb> comments = mappers.getAllComments();
        ArrayList<CommentsWeb> commentsWithout0 = new ArrayList<>();
        ArrayList<CommentsWeb> commentsWithoutShortText = new ArrayList<>();

        // Delete comments with unknown score
        for (CommentsWeb comment : comments) {
            if (comment.getScore() != 0 )
                commentsWithout0.add(comment);
        }

        for (CommentsWeb comment : commentsWithout0) {
            ArrayList<String> commentStemString = Stemer.Stem(comment.getText());
            message = "";
            for (String word : commentStemString) {
                if(word.length()>2) {
                    if (message.isEmpty())
                        message = word;
                    else
                        message = message + " " + word;
                }
            }
            comment.setText(message);
        }

        for (CommentsWeb comment: commentsWithout0){
            if(comment.getText().length() >= 4){
                commentsWithoutShortText.add(comment);
            }
        }
        CommentsToCSV.Convert3Web(commentsWithoutShortText);
    }
}
