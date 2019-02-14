package SemanticAnalysis;


import FilesHandlers.Analysis;
import FilesHandlers.JsonText;
import com.google.gson.Gson;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.model.Info;

import java.util.ArrayList;


public class CommentStem {
    private static Gson g = new Gson();
    private Stemer Stemer;

    public CommentStem(){
        Stemer = new Stemer();
    }

    public ArrayList<String> Stem(String inPut) throws MyStemApplicationException {
        ArrayList<String> outPut = new ArrayList<>();

        if (inPut == null || inPut.equals(""))
            return outPut;

        Iterable<Info> result = Stemer.Stem(inPut);

        boolean lastLex = false; // Last Gr was "не" ?

        for (Info info : result){
            JsonText jsonText = g.fromJson(info.rawResponse(), JsonText.class);
            for(Analysis analysis: jsonText.getAnalysis() ) {
                String gr = analysis.getGr();

                if (gr.contains("PART") && analysis.getLex().equals("не"))
                    lastLex = true;
                else {
                    if (lastLex) {
                        outPut.add("не" + analysis.getLex());
                        lastLex = false;
                    } else {
                        if (!(gr.contains("CONJ") || gr.contains("ADVPRO") || gr.contains("APRO")
                                || gr.contains("SPRO") || gr.contains("PR")))
                            outPut.add(analysis.getLex());
                    }
                }
            }
        }

        return outPut;
    }
}
