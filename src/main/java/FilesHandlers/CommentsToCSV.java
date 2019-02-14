package FilesHandlers;

import SemanticAnalysis.Comment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CommentsToCSV {

    public static void Convert(ArrayList<Comment> comments) throws IOException{
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/MyComments.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            for(Comment comment: comments) {
                csvPrinter.printRecord(Integer.toString(comment.getScore()),
                        comment.getText());
            }

            csvPrinter.flush();
        }
    }

    public static void Convert5(ArrayList<Comment> comments) throws IOException{
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/MyCommentsTo5.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            for(Comment comment: comments) {
                if(comment.getScore() < 3)
                    csvPrinter.printRecord(1, comment.getText());
                else
                    if(comment.getScore() < 5)
                        csvPrinter.printRecord(2, comment.getText());
                    else
                        if(comment.getScore() < 7)
                            csvPrinter.printRecord(3, comment.getText());
                        else
                            if(comment.getScore() < 9)
                                csvPrinter.printRecord(4, comment.getText());
                            else
                                csvPrinter.printRecord(5, comment.getText());
            }

            csvPrinter.flush();
        }
    }

    public static void Convert3(ArrayList<Comment> comments) throws IOException{
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/MyCommentsTo3_T.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            for(Comment comment: comments) {
                if(comment.getScore() < 4)
                    csvPrinter.printRecord("bad", comment.getText());
                else
                    if(comment.getScore() < 7)
                        csvPrinter.printRecord("normal", comment.getText());
                    else
                        csvPrinter.printRecord("good", comment.getText());
            }

            csvPrinter.flush();
        }
    }
}
