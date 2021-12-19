package projeto.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

import java.io.File;

public class Classification {
    public static void main (String[] args) throws Exception{
        //load dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff");
        Instances dataset = source.getDataSet();

        dataset.setClassIndex(dataset.numAttributes() - 1);

        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(dataset);
        System.out.println(nb.getCapabilities().toString());

        J48 tree = new J48();
        tree.buildClassifier(dataset);
        System.out.println(tree.graph());

    }
}
