package WekaAndStem;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class ScoreGetter {

    private ArrayList <Attribute> wekaAttributes;
    private String FILE = "//Applications/weka-3-8-3-oracle-jvm.app/Contents/Java/FilteredClassifier.model";
    private Classifier cls;
    private CommentStem Stemer;

    public ScoreGetter() throws Exception{
        // load classifier
        if (new File(FILE).exists()) {
            cls = (Classifier) weka.core.SerializationHelper.read(FILE);
            System.out.print("Ready for prediction\n");
        } else {
            System.out.print("Model do not exist\n");
            return;
        }
        // prepare Stem
        Stemer = new CommentStem();

        // Declare the label attribute along with its values
        ArrayList <String> classAttributeValues = new ArrayList<>();
        classAttributeValues.add("good");
        classAttributeValues.add("normal");
        classAttributeValues.add("bad");
        Attribute classAttribute = new Attribute("score", classAttributeValues);

        // Declare text attribute to hold the message
        Attribute textAttribute = new Attribute("text", (List<String>) null);

        // Declare the feature vector
        wekaAttributes = new ArrayList<>();
        wekaAttributes.add(classAttribute);
        wekaAttributes.add(textAttribute);
    }

    public String predict(String text) throws Exception{

        // Steming input text
        ArrayList<String> commentStemString = Stemer.Stem(text);
        String stemText = "";
        for (String word : commentStemString) {
            if (stemText.isEmpty())
                stemText = word;
            else
                stemText = stemText + " " + word;
        }

        // create new Instance for prediction.
        DenseInstance newInstance = new DenseInstance(2);

        // weka demand a dataset to be set to new Instance
        Instances newDataset = new Instances("predictiondata", wekaAttributes, 1);
        newDataset.setClassIndex(0);

        newInstance.setDataset(newDataset);

        // text attribute value set to value to be predicted
        newInstance.setValue(wekaAttributes.get(1), stemText);


        double pred = cls.classifyInstance(newInstance);
        // return original label
        return newDataset.classAttribute().value((int) pred);
    }

}
