package SemanticAnalysis;

import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.model.Info;

public class SemanticAnalysisApplication {
    public static void main(String args[]) throws MyStemApplicationException {
        Iterable<Info> result = Stem.Stem("И вырвал грешный мой язык");

        for (final Info info : result) {
            System.out.println(info.initial() + " -> " + info.lex() + " | " + info.rawResponse());
        }
    }
}
