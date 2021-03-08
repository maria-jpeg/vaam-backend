package projeto.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.joda.time.DateTime;
import projeto.controller.Utils;
import projeto.controller.exceptions.ParsingException;
import projeto.core.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper
{
    /*public static List<Event> csvToEvents(String path ) throws FileNotFoundException, ParsingException {
        //File directory = new File("./");
        //System.out.println(directory.getAbsolutePath());
        return csvToEvents( new FileInputStream(path) );
    }

    public static List<Event> csvToEvents(InputStream inputStream ) throws ParsingException {
        List<Event> entities = new ArrayList<>();
        CSVReader reader = new CSVReader( new InputStreamReader( inputStream ) );

        String[] nextLine;
        int lineNumber = 0;
        try
        {

            while ((nextLine = reader.readNext()) != null)
            {
                lineNumber++;
                // todo
                // nextLine[] is an array of values from the line
                //System.out.println(nextLine[0] + nextLine[1] + "etc...");

                long id;
                try {
                    id = Long.parseLong( nextLine[0] );
                } catch (NumberFormatException e) {
                    //e.printStackTrace();

                    if( lineNumber > 1 )
                        throw new IllegalArgumentException( "'" + nextLine[0] + "' is not a case id."  );

                    System.err.println( "'" + nextLine[0] + "' is not a case id." );
                    continue;
                }

                DateTime startDate = null;
                DateTime endDate = null;
                try {

                    startDate = Utils.parseDate( nextLine[1] );
                    endDate = Utils.parseDate( nextLine[2] );
                }
                catch (Exception ex) {
                    System.err.println( "Invalid date. " + ex.getMessage() );
                    throw new IllegalArgumentException( "Invalid date. " + ex.getMessage() );
                    //continue;
                }

                Event event = null;

                event = new Event(
                        id , nextLine[3], startDate, endDate,  nextLine[4], nextLine[5]
                );

                entities.add(event);

            }
        } catch (IOException | CsvValidationException e )
        {
            e.printStackTrace();
            throw new ParsingException( "Unable to parse csv file at line " + lineNumber +" : " + e.getMessage() );
        }

        return entities;
    }*/


}
