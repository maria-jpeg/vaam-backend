package projeto.weka;
import weka.*;

import java.io.FileWriter;

public class HelloWeka {
    public static void main (String[] args) throws Exception {
        System.out.println("Hello Weka");

        FileWriter fw = new FileWriter("C:\\SmartTracking\\vaam-backend\\src\\main\\java\\projeto\\weka\\datasets\\weka_add_prediction_lines.arff ",true);
        fw.write("\rpreliminares,none");
        fw.close();

        ClassifyActivitiesSequence classifier = new ClassifyActivitiesSequence();
        classifier.predictActivitySequenceLastLine("weka_add_prediction_lines.arff");
    }
}
