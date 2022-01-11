package projeto.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileWriter;

public class ClassifyActivitiesSequence {
    public static void main (String[] args) throws Exception{
        //load training dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff");
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);

        int numClasses = dataset.numClasses();
        for (int i = 0; i < numClasses; i++) {
            String classValue = dataset.classAttribute().value(i);
            System.out.println("Class Value " + i + " is " + classValue);
        }

        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(dataset);

        //J48 tree = new J48();
        //tree.buildClassifier(dataset);

        FileWriter fw = new FileWriter("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff",true);
        fw.write("\r preliminares,none");
        fw.close();

        //load dataset to predict
        DataSource sourcePredict = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff");
        Instances datasetPredict = sourcePredict.getDataSet();
        datasetPredict.setClassIndex(datasetPredict.numAttributes() - 1);

        System.out.println("====================");
        System.out.println("Actual Class, NB Predicted");
        for (int i = 0; i < datasetPredict.numInstances(); i++) {
            double actualClass = datasetPredict.instance(i).classValue();
            String actual = datasetPredict.classAttribute().value((int) actualClass);

            Instance newInstance = datasetPredict.instance(i);

            double predJ48 = nb.classifyInstance(newInstance);
            String predString = datasetPredict.classAttribute().value((int) predJ48);
            System.out.println(actual + " <><><><><><><><><><><> " + predString);
        }

        ClassifyActivitiesSequence classifier = new ClassifyActivitiesSequence();
        classifier.predictActivitySequenceLastLine("weka_add_prediction_lines.arff");

    }

    public void predictActivitySequenceLastLine(String datasetName) throws Exception{
        //load training dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff");
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);

        int numClasses = dataset.numClasses();
        for (int i = 0; i < numClasses; i++) {
            String classValue = dataset.classAttribute().value(i);
            System.out.println("Class Value " + i + " is " + classValue);
        }

        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(dataset);

        //J48 tree = new J48();
        //tree.buildClassifier(dataset);

        FileWriter fw = new FileWriter("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff",true);
        fw.write("\r preliminares,none");
        fw.close();

        //load dataset to predict
        DataSource sourcePredict = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff");
        Instances datasetPredict = sourcePredict.getDataSet();
        datasetPredict.setClassIndex(datasetPredict.numAttributes() - 1);

        System.out.println("====================");
        System.out.println("Actual Class, NB Predicted");
        for (int i = 0; i < datasetPredict.numInstances(); i++) {
            double actualClass = datasetPredict.instance(i).classValue();
            String actual = datasetPredict.classAttribute().value((int) actualClass);

            Instance newInstance = datasetPredict.instance(i);

            double predJ48 = nb.classifyInstance(newInstance);
            String predString = datasetPredict.classAttribute().value((int) predJ48);
            System.out.println(actual + " <><><><><><><><><><><> " + predString);
        }

    }

}
