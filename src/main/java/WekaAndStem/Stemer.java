package WekaAndStem;

import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.io.File;

public class Stemer {
    private MyStem myStemAnalyzer;

    Stemer() {
        myStemAnalyzer = new Factory("-igd --format json")
                        .newMyStem("3.0", Option.<File>empty()).get();
    }

    public Iterable<Info> Stem(String text) throws MyStemApplicationException{

        final Iterable<Info> result =
                JavaConversions.asJavaIterable(
                        myStemAnalyzer
                                .analyze(Request.apply(text))
                                .info()
                                .toIterable());

        return result;
    }
}
