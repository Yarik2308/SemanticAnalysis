package SemanticAnalysis;


import com.google.gson.Gson;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.model.Info;


public class CommentStem {
    private static Gson g = new Gson();

    public static String Stem(String inPut) throws MyStemApplicationException {
        if (inPut == null || inPut.equals(""))
            return "";

        Iterable<Info> result = Stem.Stem(inPut);
        String outPut = "";
        String lastGr = "";
        for (Info info : result){
            JsonText jsonText = g.fromJson(info.rawResponse(), JsonText.class);
            for(Analysis analysis: jsonText.getAnalysis() ) {
                if(lastGr.equals("PART="))
                    outPut = outPut + jsonText.getText();
                else
                    if(!analysis.getGr().equals("CONJ="))
                        outPut = outPut + " " + jsonText.getText();

                lastGr = analysis.getGr();
            }
        }

        return outPut;
    }
}
