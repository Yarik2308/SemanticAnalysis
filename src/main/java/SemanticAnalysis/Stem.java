package SemanticAnalysis;
public class Stem {
    public static String Stem(String text) {
        if (text != null) {
            text =  text.toLowerCase().replaceAll("[\\pP\\d]", " ");
        } else {
            text =  "";
        }

        // get all significant words
        String[] words = text.split("[ \n\t\r$+<>№=]");
        for (int i = 0; i < words.length; i++) {
            words[i] = PorterStemmer.doStem(words[i]);
        }
        return text;
    }
}
