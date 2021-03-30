package projeto.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.joda.time.DateTime;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import projeto.controller.Utils;
import projeto.controller.exceptions.ParsingException;
import projeto.core.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarException;

public class XESHelper
{

    public static IMLog eventsToIMLog(List<Event> events) throws JAXBException {
        //passar evento para xml?
        JAXBContext jaxbContext = JAXBContext.newInstance(EventPOJOList.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Xmlstring
        String xmlString = "";
        //Converter eventos em string xml
        StringWriter sw = new StringWriter();
        List<EventPOJO> eventPOJOS = EventPOJO.eventListToPojoList(events);
        EventPOJOList eventPOJOList = new EventPOJOList(eventPOJOS);
        jaxbMarshaller.marshal(eventPOJOList,sw);
        xmlString = sw.toString();
        //Passar de xml para XES?

        XesXmlParser parser = new XesXmlParser();

        //passar string para stream de bytes para o parse

        InputStream xmlStringStream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
        try {
            List<XLog> xlog = parser.parse(xmlStringStream);
            System.out.println("breakpoint");
        }
        catch (Exception e) {
            System.out.println("Erro a tranformar xml em xes."+e);
        }


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

//Lista simples de eventos. No lugar de ids tem o nome do objeto associado
@Getter
@Setter
class EventPOJO
{
    private String id;
    private String startDate;
    private String endDate;
    private String duration;
    private String mould;
    private String part;
    private String process;
    private String activity;
    private String isEstimatedEnd;
    private String workstation;

    public EventPOJO(Event event)
    {
        this.id = Long.toString(event.getId());
        this.startDate = event.getStartDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.endDate = event.getEndDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" );
        this.duration = Long.toString(event.getDuration());
        this.mould = event.getMould().getCode();
        if (event.getPart() != null){
            this.part = event.getPart().getCode();
        }else{
            this.part = "";
        }
        this.process = event.getProcess().getName();
        this.activity = event.getActivity().getName();
        if (event.getIsEstimatedEnd() != null){
            this.isEstimatedEnd = event.getIsEstimatedEnd().toString();
        }else{
            this.isEstimatedEnd = "";
        }
        if (event.getWorkstation() != null){
            this.workstation = event.getWorkstation().getName();
        }else{
            this.workstation = "";
        }
    }

    protected static List<EventPOJO> eventListToPojoList(List<Event> events){
        List<EventPOJO> pojoList = new LinkedList<>();

        for (Event event : events) {
            EventPOJO newPojo = new EventPOJO(event);
            pojoList.add(newPojo);
        }
        return pojoList;
    }
}
//Lista de eventos simples para serializar em xml
@Getter
@Setter
@XmlRootElement
@NoArgsConstructor
class EventPOJOList{
    private List<EventPOJO> event;

    public EventPOJOList(List<EventPOJO> events) {
        this.event = events;
    }
}
