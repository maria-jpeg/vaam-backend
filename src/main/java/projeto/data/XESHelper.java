package projeto.data;

import org.deckfour.xes.in.XesXmlParser;
import org.joda.time.DateTime;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import projeto.controller.Utils;
import projeto.controller.exceptions.ParsingException;
import projeto.core.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarException;

public class XESHelper
{

    public static IMLog eventsToIMLog(List<Event> events) throws JAXBException {

        //passar evento para xml?
        JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        StringBuilder xmlString = new StringBuilder();

        StringWriter sw = new StringWriter();

        for (Event event : events) {
            jaxbMarshaller.marshal(event,sw);
            xmlString.append(sw.toString());
        }

        XesXmlParser parser = new XesXmlParser();

        System.out.println(xmlString);
        return null;
    }

    /*
    public static List<Event> xesToEvents(String path ) throws Exception
    {
        return xesToEvents( new FileInputStream( path ) );
    }

    public static List<Event> xesToEvents( InputStream inputStream ) throws ParsingException
    {

        XesXmlParser parser = new XesXmlParser();
        //if (!parser.canParse(xesFileIn))
        //    throw new IllegalArgumentException( "Unable to parse " + path );
        // can parse only checks extension

        List<XLog> xLogs = null;
        try {
            xLogs = parser.parse( inputStream );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParsingException( e.getMessage() );
        }

        List<Event> eventsList = new ArrayList<>();
        HashMap<Long, Event> startedActivities = new HashMap<>();
        for ( XLog log : xLogs)
        {
            for ( XTrace trace : log )
            {
                long caseID;
                try {
                    XAttributeMap attrs = trace.getAttributes();
                    String caseIDStr = attrs.get("concept:name").toString();
                    caseID = Long.parseLong( caseIDStr );
                } catch (NumberFormatException e) {

                    throw new IllegalArgumentException( "Error parsing case id: " + e.getMessage() );
                }

                for ( XEvent xEvent : trace )
                {

                    XAttributeMap attrs = xEvent.getAttributes();
                    String state = attrs.get("lifecycle:transition").toString();
                    String idStr = attrs.get("concept:instance").toString();

                    long id;
                    try {
                        id = Long.parseLong( idStr );
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                        //System.err.println( "'" + idStr + "' is not a valid number." );
                        throw new IllegalArgumentException( "'" + idStr + "' is not a valid number." );
                    }

                    DateTime date = null;
                    try {

                        date = Utils.parseDate( attrs.get("time:timestamp").toString() );
                    }
                    catch (Exception ex) {
                        //System.err.println( "Invalid date. " + ex.getMessage() );
                        throw new IllegalArgumentException( "Invalid date. " + ex.getMessage() );
                        //continue;
                    }
                    Event event = null;

                    if( state.equals( "complete" ) )
                    {
                        event = startedActivities.get( id );
                        if( event != null )
                        {
                            event.setEnd( date );
                            eventsList.add( event );
                            startedActivities.remove( id );
                        }
                        continue;
                    }

                    try {
                        event = new Event(
                                caseID,
                                attrs.get("concept:name").toString(),
                                date,
                                null,
                                attrs.get("Resource").toString(),
                                attrs.get("Role").toString()
                        );
                        startedActivities.put( id, event );

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        //continue;
                    }
                }


            }
        }

        return eventsList;

    }*/

}
