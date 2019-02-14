package FilesHandlers;

import java.util.ArrayList;

public class JsonText {
    private String text;
    private ArrayList<Analysis> analysis;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Analysis> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(ArrayList<Analysis> analysis) {
        this.analysis = analysis;
    }
}