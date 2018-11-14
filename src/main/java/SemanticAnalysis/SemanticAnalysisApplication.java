package SemanticAnalysis;

import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.model.Info;

public class SemanticAnalysisApplication {
    public static void main(String args[]) throws MyStemApplicationException {
        Iterable<Info> result = Stem.Stem("Не фильм, а глюк!");

        for (final Info info : result) {
            System.out.println(info.initial() + " -> " + info.lex() + " | " + info.rawResponse());
        }

        String commentStemString = CommentStem.Stem("Не фильм, а глюк!");
        System.out.print(commentStemString);
    }
}
