package projeto.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;


public class LoadSaveData {
    public static void main (String[] args) throws Exception{
        String path = System.getProperty("user.dir");
        System.out.println(path);

        CSVLoader loader = new CSVLoader();
        loader.setSource(new File( "C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\PRODUCAO_pedal_previous_next_activities_no_nulls.csv"));
        Instances dataset = loader.getDataSet();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataset);
        saver.setFile(new File("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\new_PRODUCAO_pedal_previous_next_activities.arff"));
        saver.writeBatch();

    }
}
