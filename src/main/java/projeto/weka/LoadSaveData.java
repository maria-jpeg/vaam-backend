package projeto.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class LoadSaveData {
    public static void main (String[] args) throws Exception{
        String path = System.getProperty("user.dir");
        System.out.println(path);

        //CSVLoader loader = new CSVLoader();
        //loader.setSource(new File( "C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset.csv"));
        //Instances dataset = loader.getDataSet();
        Instances dataset = new Instances(new BufferedReader(new FileReader("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_dataset.arff")));

        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataset);
        saver.setFile(new File("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\new_weka_dataset.arff"));
        saver.writeBatch();

    }
}
