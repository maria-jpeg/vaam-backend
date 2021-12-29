package projeto.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

public class AttributeFilter {
    public static void main (String[] args) throws Exception{
        //load dataset
        DataSource source = new DataSource("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\new_WEKA_dataset.arff");
        Instances dataset = source.getDataSet();

        //use a simple filter to remove certain attributes
        String[] opts = new String[]{"-R","7,8,11"}; //molde_area, complexity, subcontrating, sub_process
        Remove remove = new Remove();
        remove.setOptions(opts);
        remove.setInputFormat(dataset);
        Instances newData = Filter.useFilter(dataset,remove);

        ArffSaver saver = new ArffSaver();
        saver.setInstances(newData);
        saver.setFile(new File("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset_filtered_attributes.arff"));
        saver.writeBatch();

    }
}
