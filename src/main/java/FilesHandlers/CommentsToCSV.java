package FilesHandlers;

import WebCrawler.CommentsWeb;
import WekaAndStem.Comment;
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
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/MyComments3Short4567.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            ////6789
//            for(Comment comment: comments) {
//                if(comment.getScore() < 7)
//                    csvPrinter.printRecord("bad", comment.getText());
//                else
//                    if(comment.getScore() < 9)
//                        csvPrinter.printRecord("normal", comment.getText());
//                    else
//                        csvPrinter.printRecord("good", comment.getText());
//            }
            ////3478
////            for(Comment comment: comments) {
////                if(comment.getScore() < 4)
////                    csvPrinter.printRecord("bad", comment.getText());
////                else
////                if(comment.getScore() < 8)
////                    csvPrinter.printRecord("normal", comment.getText());
////                else
////                    csvPrinter.printRecord("good", comment.getText());
////            }
            ////3467
//            for(Comment comment: comments) {
//                if(comment.getScore() < 4)
//                    csvPrinter.printRecord("bad", comment.getText());
//                else
//                if(comment.getScore() < 7)
//                    csvPrinter.printRecord("normal", comment.getText());
//                else
//                    csvPrinter.printRecord("good", comment.getText());
//            }
            ////4567
            for(Comment comment: comments) {
                if(comment.getScore() < 5)
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


    public static void Convert3Web6789(ArrayList<CommentsWeb> comments) throws IOException{
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/TestComments3Short6789.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            ////6789
            for(CommentsWeb comment: comments) {
                if(comment.getScore() < 7)
                    csvPrinter.printRecord("bad", comment.getText());
                else
                if(comment.getScore() < 9)
                    csvPrinter.printRecord("normal", comment.getText());
                else
                    csvPrinter.printRecord("good", comment.getText());
            }

            csvPrinter.flush();
        }
    }

    public static void Convert3Web3478(ArrayList<CommentsWeb> comments) throws IOException {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/TestComments3Short3478.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            //3478
            for(CommentsWeb comment: comments) {
                if(comment.getScore() < 4)
                    csvPrinter.printRecord("bad", comment.getText());
                else
                if(comment.getScore() < 8)
                    csvPrinter.printRecord("normal", comment.getText());
                else
                    csvPrinter.printRecord("good", comment.getText());
            }
            csvPrinter.flush();
        }
    }

    public static void Convert3Web3467(ArrayList<CommentsWeb> comments) throws IOException {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/TestComments3Short3467.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            //3467
            for(CommentsWeb comment: comments) {
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

    public static void Convert3Web4567(ArrayList<CommentsWeb> comments) throws IOException {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/TestComments3Short4567.csv"));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("score", "text"));
        ) {
            //4567
            for(CommentsWeb comment: comments) {
                if(comment.getScore() < 5)
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
