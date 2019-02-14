package FilesHandlers;

import SemanticAnalysis.Comment;
import SemanticAnalysis.CommentStem;
import SemanticAnalysis.XMLParser;
import org.xml.sax.SAXException;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class FilesHandler {
    public static void main(String args[]) throws MyStemApplicationException, IOException,
            ParserConfigurationException, SAXException {

        ArrayList<Comment> comments = XMLParser.parse();
        ArrayList<Comment> commentsWithout0 = new ArrayList<>();

        // Delete comments with unknown score
        for (Comment comment : comments) {
            if (comment.getScore() != 0)
                commentsWithout0.add(comment);
        }

        // Create CSV file for Weka GUI.
        String message;
        CommentStem Stemer = new CommentStem();
        for (Comment comment : commentsWithout0) {
            ArrayList<String> commentStemString = Stemer.Stem(comment.getText());
            message = "";
            for (String word : commentStemString) {
                if (message.isEmpty())
                    message = word;
                else
                    message = message + " " + word;
            }
            comment.setText(message);
        }

        CommentsToCSV.Convert(commentsWithout0);
        CommentsToCSV.Convert5(commentsWithout0);
        CommentsToCSV.Convert3(commentsWithout0);
    }
}
