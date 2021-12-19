package projeto.weka;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Evaluate {
    public static void main (String[] args) throws Exception{
        //load dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff");
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);

        J48 tree = new J48();
        tree.buildClassifier(dataset);

        Evaluation eval = new Evaluation(dataset);
        Random rand = new Random(1);
        int folds = 10;

        DataSource sourceTest = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff");
        Instances datasetTest = sourceTest.getDataSet();
        datasetTest.setClassIndex(datasetTest.numAttributes() - 1);

        //eval.evaluateModel(tree, datasetTest);
        eval.crossValidateModel(tree, datasetTest,folds,rand);
        System.out.println(eval.toSummaryString("Evaluation Results:\n", false));

        System.out.println("Correct% = " + eval.pctCorrect());
        System.out.println("Incorrect% = " + eval.pctIncorrect());
        System.out.println("Error Rate = " + eval.errorRate());

        //confusion matrix
        //System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
    }
}
