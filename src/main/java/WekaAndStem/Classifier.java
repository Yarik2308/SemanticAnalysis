package WekaAndStem;


import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
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
import java.util.Random;


public class Classifier {
    static final String dataPath = "src/main/resources/MyComments.arff";
    static final String stopWordsPath = "src/main/resources/russianST.txt";

    public static void main(String args[]){

        boolean result = createFilteredClassifier();
        System.out.println(result);
        return;
    }

    public static Instances getFilteredData(){

        try {
            DataSource source = new DataSource(dataPath);
            Instances data = source.getDataSet();
            // setting class attribute if the data format does not provide this information
            if (data.classIndex() == -1)
                data.setClassIndex(0);

            //////// Filtering
            //// StringToWordVector
            StringToWordVector STWV = new StringToWordVector();
            WordsFromFile stopWords = new WordsFromFile();
            stopWords.setStopwords(new File(stopWordsPath));

            STWV.setIDFTransform(true);
            STWV.setTFTransform(true);
            STWV.setDoNotOperateOnPerClassBasis(true);
            STWV.setStopwordsHandler(stopWords);

            //// AttributeSelection
            AttributeSelection selection = new AttributeSelection();
            selection.setEvaluator(new InfoGainAttributeEval());
            Ranker search = new Ranker();
            search.setThreshold(0);
            selection.setSearch(search);


            //// Resample
            Resample resample = new Resample();
            resample.setBiasToUniformClass(1);

            //// Filtering
            STWV.setInputFormat(data);
            Instances dataStwv = Filter.useFilter(data, STWV);

            selection.setInputFormat(dataStwv);
            Instances dataStwvAndAs = Filter.useFilter(dataStwv, selection);

            resample.setInputFormat(dataStwvAndAs);
            Instances finalData = Filter.useFilter(dataStwvAndAs, resample);

            return finalData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean createClassifier(){

        try {
            String[] classifierOptions = weka.core.Utils.splitOptions(
                    "-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");

            RandomForest classifier = new RandomForest();
            classifier.setOptions(classifierOptions);

            Instances data = getFilteredData();

            Evaluation eval = new Evaluation(data);
            System.out.println("CrossValidation begin");
            eval.crossValidateModel(classifier, data,10, new Random());
            System.out.println(eval.toSummaryString("\nResults\n======\n", true));
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());


            //System.out.println("Ready for train");
            //classifier.buildClassifier(data);
            //weka.core.SerializationHelper.write("src/main/resources/MyModel.model", classifier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean createFilteredClassifier(){

        try {
            DataSource source = new DataSource(dataPath);
            Instances data = source.getDataSet();
            // setting class attribute if the data format does not provide this information
            if (data.classIndex() == -1)
                data.setClassIndex(0);

            //////// Filtering

            //// StringToWordVector
            StringToWordVector STWV = new StringToWordVector();
            WordsFromFile stopWords = new WordsFromFile();
            stopWords.setStopwords(new File(stopWordsPath));

            STWV.setIDFTransform(true);
            STWV.setTFTransform(true);
            STWV.setDoNotOperateOnPerClassBasis(true);
            STWV.setOutputWordCounts(true);
            STWV.setStopwordsHandler(stopWords);
            //STWV.setInputFormat(data);

            //// AttributeSelection
            AttributeSelection selection = new AttributeSelection();
            selection.setEvaluator(new InfoGainAttributeEval());
            Ranker search = new Ranker();
            search.setThreshold(0);
            selection.setSearch(search);

            //// Resample
            Resample resample = new Resample();
            resample.setBiasToUniformClass(1);
            //resample.setNoReplacement(true);

            String[] classifierOptions = weka.core.Utils.splitOptions(
                    "-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1");

            RandomForest classifier = new RandomForest();
            classifier.setOptions(classifierOptions);

            //Instances data = getFilteredData();
            Evaluation eval = new Evaluation(data);

            FilteredClassifier filteredClassifier = new FilteredClassifier();
            MultiFilter multiFilter = new MultiFilter();
            multiFilter.setFilters(new Filter[]{STWV, selection});
            multiFilter.setInputFormat(data);

            filteredClassifier.setClassifier(classifier);
            filteredClassifier.setFilter(multiFilter);

            System.out.println("CrossValidation begin");
            eval.crossValidateModel(filteredClassifier, data,10, new Random());
            System.out.println(eval.toSummaryString("\nResults\n======\n", true));
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());


//            System.out.println("Ready for train");
//            filteredClassifier.buildClassifier(data);
//
//            filteredClassifier.setFilter(STWV);
//            weka.core.SerializationHelper.write("src/main/resources/FilteredClassifier.model", filteredClassifier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
