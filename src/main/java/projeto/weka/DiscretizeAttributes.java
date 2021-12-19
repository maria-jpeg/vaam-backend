package projeto.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

public class DiscretizeAttributes {
    public static void main (String[] args) throws Exception{
        //load dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_filtered_attributes.arff");
        Instances dataset = source.getDataSet();

        //use a simple filter to remove certain attributes
        String[] opts = new String[4];

        //Discretize by binning
        opts[0] = "-B"; opts[1] = "2";
        opts[2] = "-R"; opts[3] = "first-last";

        Discretize discretize = new Discretize();
        discretize.setOptions(opts);
        discretize.setInputFormat(dataset);
        Instances newData = Filter.useFilter(dataset,discretize);


        ArffSaver saver = new ArffSaver();
        saver.setInstances(newData);
        saver.setFile(new File("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_discretized_attributes.arff"));
        saver.writeBatch();

    }
}
