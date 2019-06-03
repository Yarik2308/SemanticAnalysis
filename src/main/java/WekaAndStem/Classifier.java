package WekaAndStem;


import WebCrawler.CommentsWeb;
import WebCrawler.Mappers;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stopwords.WordsFromFile;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Classifier {
    //done
    static final String dataRomip3467 = "src/main/resources/MyComments3Short3467.arff";
    static final String dataRomip3478 = "src/main/resources/MyComments3Short3478.arff";
    static final String dataRomip4567 = "src/main/resources/MyComments3Short6789.arff";
    static final String dataRomip6789 = "src/main/resources/MyComments3Short6789.arff";

    static final String dataMegacritic3467 = "src/main/resources/TestComments3Short3467.arff";
    static final String dataMegacritic3478 = "src/main/resources/TestComments3Short3478.arff";
    static final String dataMegacritic4567 = "src/main/resources/TestComments3Short4567.arff";
    static final String dataMegacritic6789 = "src/main/resources/TestComments3Short6789.arff";

    static final String stopWordsPath = "src/main/resources/russianST.txt";

    private static Instances data;
    private static StringToWordVector STWV;
    private static AttributeSelection selection;
    private static Resample resample;
    private static RandomForest classifier;
    private static FilteredClassifier filteredClassifier;


    public static void main(){
        try {
            String[] classifierOptions = weka.core.Utils.splitOptions(
                    "-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");

            classifier = new RandomForest();
            classifier.setOptions(classifierOptions);
            filteredClassifier = new FilteredClassifier();
            filteredClassifier.setClassifier(classifier);

            //Evaluation
            evaluateFromDB();

//            System.out.println("ROMIP 1<=bad<=3 4<=normal<=6 7<=good<=10");
//            evaluateFiltered(dataRomip3467);
//            evaluatePreprocessed(dataRomip3467);

//            System.out.println("ROMIP 1<=bad<=3 4<=normal<=7 8<=good<=10");
//            evaluateFiltered(dataRomip3478);
//            evaluatePreprocessed(dataRomip3478);

//            System.out.println("ROMIP 1<=bad<=4 5<=normal<=6 7<=good<=10");
//            evaluateFiltered(dataRomip4567);
//            evaluatePreprocessed(dataRomip4567);

//            System.out.println("ROMIP 1<=bad<=6 7<=normal<=8 9<=good<=10");
//            evaluateFiltered(dataRomip6789);
//            evaluatePreprocessed(dataRomip6789);

//            System.out.println("Megacritic 1<=bad<=3 4<=normal<=6 7<=good<=10");
//            evaluateFiltered(dataMegacritic3467);
//            evaluatePreprocessed(dataMegacritic3467);
//
//            System.out.println("Megacritic 1<=bad<=3 4<=normal<=7 8<=good<=10");
//            evaluateFiltered(dataMegacritic3478);
//            evaluatePreprocessed(dataMegacritic3478);
//
//            System.out.println("Megacritic 1<=bad<=4 5<=normal<=6 7<=good<=10");
//            evaluateFiltered(dataMegacritic4567);
//            evaluatePreprocessed(dataMegacritic4567);
//
//            System.out.println("Megacritic 1<=bad<=6 7<=normal<=8 9<=good<=10");
//            evaluateFiltered(dataMegacritic6789);
//            evaluatePreprocessed(dataMegacritic6789);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    public static Instances filtersAndDataPreprocess(int n){
        try {
            // setting class attribute if the data format does not provide this information
            if (data.classIndex() == -1)
                data.setClassIndex(0);

            //////// Filtering
            //// StringToWordVector
            STWV = new StringToWordVector();
            WordsFromFile stopWords = new WordsFromFile();
            stopWords.setStopwords(new File(stopWordsPath));

            STWV.setIDFTransform(true);
            STWV.setTFTransform(true);
            STWV.setDoNotOperateOnPerClassBasis(true);
            STWV.setOutputWordCounts(true);
            STWV.setStopwordsHandler(stopWords);
            STWV.setInputFormat(data);

            Instances afterSTWV = Filter.useFilter(data, STWV);
            if(n==1){
                return afterSTWV;
            }

            //// AttributeSelection
            selection = new AttributeSelection();
            selection.setEvaluator(new InfoGainAttributeEval());
            Ranker search = new Ranker();
            search.setThreshold(0);
            selection.setSearch(search);
            selection.setInputFormat(afterSTWV);
            Instances afterSelection = Filter.useFilter(afterSTWV, selection);

            if(n==2){
                return afterSelection;
            }
            //// Resample
            resample = new Resample();
            resample.setBiasToUniformClass(1);
            resample.setInputFormat(afterSelection);

            if(n==3) {
                return Filter.useFilter(afterSelection, resample);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void evalFiltered(){
        Evaluation eval = null;
        try {
            eval = new Evaluation(data);
            System.out.println("CrossValidation begin");
            eval.crossValidateModel(filteredClassifier, data,10, new Random());
            System.out.println(eval.toSummaryString("\nResults\n======\n", true));
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evalPreprocesed(Instances data){
        Evaluation eval = null;
        try {
            eval = new Evaluation(data);
            System.out.println("CrossValidation begin");
            eval.crossValidateModel(classifier, data,10, new Random());

            System.out.println(eval.toSummaryString("\nResults\n======\n", true));
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean evaluateFiltered(String dataPath){
        try {
            DataSource source = null;
            source = new DataSource(dataPath);
            data = source.getDataSet();

            MultiFilter multiFilter = new MultiFilter();

            System.out.println("FilteredClassifier RandomForest StringToWordVector crossvalidation");
            filtersAndDataPreprocess(0);

            filteredClassifier.setFilter(STWV);
            evalFiltered();

            System.out.println("FilteredClassifier RandomForest StringToWordVector AttributeSelector " +
                    "crossvalidation");

            multiFilter.setFilters(new Filter[]{STWV, selection});
            multiFilter.setInputFormat(data);
            filteredClassifier.setFilter(multiFilter);
            evalFiltered();

            System.out.println("FilteredClassifier RandomForest StringToWordVector AttributeSelector " +
                    "Resample crossvalidation");

            multiFilter.setFilters(new Filter[]{STWV, selection, resample});
            multiFilter.setInputFormat(data);
            filteredClassifier.setFilter(multiFilter);
            evalFiltered();

            //System.out.println("Ready for train");
            //filteredClassifier.buildClassifier(data);

            //weka.core.SerializationHelper.write("src/main/resources/FilteredClassifier.model", filteredClassifier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean evaluatePreprocessed(String dataPath){
        try{
            DataSource source = null;
            source = new DataSource(dataPath);
            data = source.getDataSet();

            System.out.println("Preprocessed RandomForest StringToWordVector crossvalidation");
            Instances prepData =  filtersAndDataPreprocess(1);
            evalPreprocesed(prepData);

            System.out.println("Preprocessed RandomForest StringToWordVector AttributeSelector " +
                    "crossvalidation");
            prepData =  filtersAndDataPreprocess(2);
            evalPreprocesed(prepData);

            System.out.println("Preprocessed RandomForest StringToWordVector AttributeSelector " +
                    "Resample crossvalidation");
            prepData =  filtersAndDataPreprocess(3);
            evalPreprocesed(prepData);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean evaluateFromDB(){
        try{
            // prepare Stem
            CommentStem stemer = new CommentStem();

            System.out.println("Geting data from DataBase");
            Mappers mappers = new Mappers();
            List<CommentsWeb> comments = mappers.getComments();

            ArrayList<String> classAttributeValues = new ArrayList<>();
            classAttributeValues.add("bad");
            classAttributeValues.add("normal");
            classAttributeValues.add("good");
            Attribute classAttribute = new Attribute("score", classAttributeValues);

            // Declare text attribute to hold the message
            Attribute textAttribute = new Attribute("text", (List<String>) null);

            // Declare the feature vector
            ArrayList <Attribute> wekaAttributes;
            wekaAttributes = new ArrayList<>();
            wekaAttributes.add(classAttribute);
            wekaAttributes.add(textAttribute);

            data = new Instances("predictiondata", wekaAttributes, 0);

            for(CommentsWeb comment: comments){
                // create new Instance for prediction.
                Instance newInstance = new DenseInstance(2);
                newInstance.setDataset(data);

                ArrayList<String> commentStemString = stemer.Stem(comment.getText());
                String stemText = "";
                for (String word : commentStemString) {
                    if (stemText.isEmpty())
                        stemText = word;
                    else
                        stemText = stemText + " " + word;
                }

                newInstance.setValue(wekaAttributes.get(1), stemText);

                if(comment.getScore()<5){
                    newInstance.setValue(wekaAttributes.get(0), "bad");
                } else if(comment.getScore()<7){
                    newInstance.setValue(wekaAttributes.get(0), "normal");
                } else {
                    newInstance.setValue(wekaAttributes.get(0), "good");
                }

                data.add(newInstance);
            }

            System.out.println("Preparing filters");
            filtersAndDataPreprocess(0);
            MultiFilter multiFilter = new MultiFilter();
            multiFilter.setFilters(new Filter[]{STWV, selection, resample});
            multiFilter.setInputFormat(data);

            filteredClassifier.setFilter(multiFilter);

            System.out.println("Creating Classifier");
            filteredClassifier.buildClassifier(data);
            weka.core.SerializationHelper.write("src/main/resources/FilteredClassifier.model",
                    filteredClassifier);

            ////// Data analysis
            System.out.println("Data analysis");
            ScoreGetter getter = new ScoreGetter();
            for(CommentsWeb comment: comments){
                String result = getter.predict(comment.getText());
                if(result == "good"){
                    comment.setScoreMLT(3);
                } else if(result == "normal") {
                    comment.setScoreMLT(2);
                } else{
                    comment.setScoreMLT(1);
                }
                mappers.updateCommentMLT(comment.getId(), comment.getScoreMLT());
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
