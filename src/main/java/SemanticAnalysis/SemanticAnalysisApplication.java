package SemanticAnalysis;

import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.model.Info;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.trees.RandomForest;

import java.util.ArrayList;
import java.util.Arrays;

public class SemanticAnalysisApplication {
    public static void main(String args[]) throws MyStemApplicationException {

        // Stem test
//        Iterable<Info> result = Stem.Stem("И вырвал грешный мой язык, а не исправил");
//
//        for (final Info info : result) {
//            System.out.println(info.initial() + " -> " + info.lex() + " | " + info.rawResponse());
//        }
//        ArrayList<String> commentStemString = CommentStem.Stem("И вырвал грешный мой язык, а не исправил");
//        System.out.print(commentStemString);

        // Weka test
        try {
            TextClassifier cl = new TextClassifier(new RandomForest());
            cl.addCategory("computer");
            cl.addCategory("sport");
            cl.addCategory("unknown");
            cl.setupAfterCategorysAdded();

            //
            cl.addData("cs", "computer");
            cl.addData("java", "computer");
            cl.addData("soccer", "sport");
            cl.addData("snowboard", "sport");

            double[] result = cl.classifyMessage("java");
            System.out.println("====== RESULT ====== \tCLASSIFIED AS:\t" + Arrays.toString(result));

            result = cl.classifyMessage("asdasdasd");
            System.out.println("====== RESULT ======\tCLASSIFIED AS:\t" + Arrays.toString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
