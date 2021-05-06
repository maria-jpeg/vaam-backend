package projeto.data;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deckfour.xes.model.XLog;
import org.processmining.log.csv.CSVFileReferenceUnivocityImpl;
import org.processmining.log.csv.ICSVReader;
import org.processmining.log.csv.config.CSVConfig;
import org.processmining.log.csvimport.CSVConversion;
import org.processmining.log.csvimport.config.CSVConversionConfig;
import projeto.core.Event;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.*;

public class XESHelper
{
    /*
    public static String eventsToXml(List<Event> events) throws JAXBException {
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

        return xmlString;
    }
    */

    public static String eventsToCsv(List<Event> events){
        StringBuilder csvString = new StringBuilder("id,activity,process,mouldCode,partCode,startDate,endDate,duration,isEstimatedEnd,workstation\n");
        List<EventPOJO> eventPOJOSList = EventPOJO.eventListToPojoList(events);
        for (EventPOJO eventPOJO : eventPOJOSList) {
            csvString.append(eventPOJO.getId()).append(",");
            csvString.append(eventPOJO.getActivity()).append(",");
            csvString.append(eventPOJO.getProcess()).append(",");
            csvString.append(eventPOJO.getMould()).append(",");
            csvString.append(eventPOJO.getPart()).append(",");
            csvString.append(eventPOJO.getStartDate()).append(",");
            csvString.append(eventPOJO.getEndDate()).append(",");
            csvString.append(eventPOJO.getDuration()).append(",");
            csvString.append(eventPOJO.getIsEstimatedEnd()).append(",");
            csvString.append(eventPOJO.getWorkstation()).append("\n");
        }
        return csvString.toString();
    }

    public static XLog eventsCsvToXes(String csvString){
        File tempFile;
        CSVFileReferenceUnivocityImpl csvFile;
        try{
            //Escrever para ficheiro temp
            tempFile = File.createTempFile("events-", ".csv");

            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.getPath(), true));
            writer.append(csvString);
            writer.close();
            //Ler do ficheiro temp e converter
            csvFile = new CSVFileReferenceUnivocityImpl(tempFile.toPath());
            CSVConfig config = new CSVConfig(csvFile);
            ICSVReader reader = csvFile.createReader(config);
            CSVConversion conversion = new CSVConversion();
            CSVConversionConfig conversionConfig = new CSVConversionConfig(csvFile, config);
            conversionConfig.autoDetect();

            //O case é o mould porque nos apenas vamos buscar um processo.
            //conversionConfig.setCaseColumns(ImmutableList.of("process"));
            conversionConfig.setCaseColumns(ImmutableList.of("mouldCode"));
            conversionConfig.setEventNameColumns(ImmutableList.of("activity"));

            conversionConfig.setStartTimeColumn("startDate");
            conversionConfig.setCompletionTimeColumn("endDate");
            conversionConfig.setEmptyCellHandlingMode(CSVConversionConfig.CSVEmptyCellHandlingMode.SPARSE);
            conversionConfig.setErrorHandlingMode(CSVConversionConfig.CSVErrorHandlingMode.ABORT_ON_ERROR);
            Map<String, CSVConversionConfig.CSVMapping> conversionMap = conversionConfig.getConversionMap();


            CSVConversionConfig.CSVMapping mappingStartDate = conversionMap.get("startDate");
            mappingStartDate.setDataType(CSVConversionConfig.Datatype.TIME);
            mappingStartDate.setPattern("dd-MM-yyyy HH:mm:ss.SSS");

            CSVConversionConfig.CSVMapping mappingEndDate = conversionMap.get("endDate");
            mappingEndDate.setDataType(CSVConversionConfig.Datatype.TIME);
            mappingEndDate.setPattern("dd-MM-yyyy HH:mm:ss.SSS");

            CSVConversion.ConversionResult<XLog> result = conversion.doConvertCSVToXES(new CSVConversion.NoOpProgressListenerImpl(), csvFile, config,
                    conversionConfig);


            tempFile.delete();

            XLog log = result.getResult();

            return log;

        }catch (Exception e){
            System.out.println("Erro a escrever ficheiro: "+e);
        }
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
            this.part = "null";
        }
        this.process = event.getProcess().getName();
        this.activity = event.getActivity().getName();
        if (event.getIsEstimatedEnd() != null){
            this.isEstimatedEnd = event.getIsEstimatedEnd().toString();
        }else{
            this.isEstimatedEnd = "null";
        }
        if (event.getWorkstation() != null){
            this.workstation = event.getWorkstation().getName();
        }else{
            this.workstation = "null";
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
/*
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
*/
