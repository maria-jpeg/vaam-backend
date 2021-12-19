package projeto.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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

        J48 tree = new J48();
        tree.buildClassifier(dataset);

        //load dataset to predict
        DataSource sourcePredict = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\new_PRODUCAO_pedal_previous_next_activities.arff");
        Instances datasetPredict = sourcePredict.getDataSet();
        datasetPredict.setClassIndex(datasetPredict.numAttributes() - 1);

        System.out.println("====================");
        System.out.println("Actual Class, J48 Predicted");
        for (int i = 0; i < datasetPredict.numInstances(); i++) {
            double actualClass = datasetPredict.instance(i).classValue();
            String actual = datasetPredict.classAttribute().value((int) actualClass);

            Instance newInstance = datasetPredict.instance(i);

            double predJ48 = tree.classifyInstance(newInstance);
            String predString = datasetPredict.classAttribute().value((int) predJ48);
            System.out.println(actual + " <><><><><><><><><><><><><><><><><><><><><> " + predString);
        }

    }
}
