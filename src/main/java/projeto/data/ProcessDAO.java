package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.*;
import projeto.core.Process;

import java.util.List;
import java.util.Set;

public class ProcessDAO extends BaseDAO<Process, ProcessDTO> {

    public ProcessDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Process toEntity(ProcessDTO dto) throws TransformToEntityException {

        Process process;

        try {
            process = findOrFail(dto.getId());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        // Fields allowed to change
        process.setDescription( dto.getDescription() );

        return process;
    }

    @Override
    public ProcessDTO toDTO(Process entity)
    {
        return new ProcessDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNumberOfCases(),
                entity.getNumberOfActivities()
        );

    }

    public List<Event> getEventsByProcessID(long processId ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Process.getEventsByProcessID", Event.class );
        query.setParameter( "processId", processId );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem eventos associados ao processo '" + processId + "'." );

        return events;
    }

    public List<Activity> getActivitiesfromProcessId(long processId ) throws EntityDoesNotExistException
    {
        Query<Activity> query = currentSession().createNamedQuery( "Process.getActivities", Activity.class );
        query.setParameter( "processId", processId );
        List<Activity> activities = query.getResultList();

        if( activities.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem atividades associadas ao processo " + processId );

        return activities;
    }

    public List<Workstation> getWorkstationsfromProcessId(long processId ) throws EntityDoesNotExistException
    {
        Query<Workstation> query = currentSession().createNamedQuery( "Process.getWorkstations", Workstation.class );
        query.setParameter( "processId", processId );
        List<Workstation> workstations = query.getResultList();

        if( workstations.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem workstations associadas ao processo " + processId );

        return workstations;
    }

    public List<ActivityUserEntry> getUsersFromProcessId(long processId ) throws EntityDoesNotExistException
    {
        Query<ActivityUserEntry> query = currentSession().createNamedQuery( "Process.getUsers", ActivityUserEntry.class );
        Process process = findOrFail(processId);

        DateTime processStartDate = process.getStartDate();
        DateTime processEndDate = process.getEndDate();
        if(processStartDate == null){
            processStartDate = DateTime.parse("1111-01-01T00:00:00");
        }
        if(processEndDate == null){
            processEndDate = DateTime.now();
        }

        query.setParameter( "processStartDate", processStartDate );
        query.setParameter( "processEndDate", processEndDate );
        List<ActivityUserEntry> entries = query.getResultList();

        if( entries.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem entradas (atividade-utilizador) associadas ao processo " + processId );

        return entries;
    }

    public Process findById( long id)
    {
        Query<Process> query = currentSession().createNamedQuery( "Process.checkId", Process.class );
        query.setParameter( "id", id );

        List results = query.getResultList();
        if (results.isEmpty()){
            return null;
        }
        else if (results.size() == 1){
            return (Process) results.get(0);
        }

        return query.getSingleResult();
    }

    public List<Process> findName(String name){
        return (List<Process>) currentSession().createNamedQuery("Process.getProcessesByName").setParameter("name", name).getResultList();
    }

    public Process findSubProcess(long id) throws Exception {
        Process process = findById(id);
        return process.getSubProcess();
    }

    public List<Event> getEventsFromMouldsfromProcess(long processId, List<String> moulds ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Event.getMoulds", Event.class );
        query.setParameter( "processId", processId );
        query.setParameterList( "moulds", moulds );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem eventos associados aos moldes." );

        return events;
    }

    public List<Event> getEventsFromPartsfromProcess(long processId, List<String> parts ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Event.getParts", Event.class );
        query.setParameter( "processId", processId );
        query.setParameterList( "parts", parts );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem eventos associados às peças." );

        return events;
    }

    public List<Event> getEventsFromMouldsfromProcessSet(long processId, Set<String> moulds ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Event.getMoulds", Event.class );
        query.setParameter( "processId", processId );
        query.setParameterList( "moulds", moulds );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem eventos associados aos moldes." );

        return events;
    }

    public List<Event> getEventsFromPartsfromProcessSet(long processId, Set<String> parts ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Event.getParts", Event.class );
        query.setParameter( "processId", processId );
        query.setParameterList( "parts", parts );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem eventos associados às peças." );

        return events;
    }

    public List<String> getMouldCodesfromProcessId( long processId ) throws EntityDoesNotExistException
    {
        Query<String> query = currentSession().createNamedQuery( "Process.getMouldCodesFromProcessId", String.class );
        query.setParameter( "processId", processId );
        List<String> moulds = query.getResultList();

        if( moulds.size() == 0 )
            throw new EntityDoesNotExistException( "Não existem moldes associados ao processo '" + processId + "'." );

        return moulds;
    }

    public List<String> getPartCodesfromProcessId( long processId ) throws EntityDoesNotExistException
    {
        Query<String> query = currentSession().createNamedQuery( "Process.getPartCodesFromProcessId", String.class );
        query.setParameter( "processId", processId );
        List<String> parts = query.getResultList();

        if( parts.size() == 0 )
            throw new EntityDoesNotExistException("Não existem peças associadas ao processo '" + processId + "'.");

        return parts;
    }

    public List<ActivityUserEntry> getEntriesAssociatedToEventActivity(long activityId, DateTime eventStartDate, DateTime eventEndDate)
    {
        Query<ActivityUserEntry> query = currentSession().createNamedQuery( "ActivitiesUsers.getEntriesAssociatedToEventActivity", ActivityUserEntry.class );
        query.setParameter( "activityId", activityId );
        query.setParameter( "eventStartDate", eventStartDate );
        query.setParameter( "eventEndDate", eventEndDate );
        List<ActivityUserEntry> entries = query.getResultList();

        return entries;
    }

    public List<ActivityUserEntry> getEntriesAssociatedToEventWorkstation(long workstationId, DateTime eventStartDate, DateTime eventEndDate)
    {
        Query<ActivityUserEntry> query = currentSession().createNamedQuery( "ActivitiesUsers.getEntriesAssociatedToEventWorkstation", ActivityUserEntry.class );
        query.setParameter( "workstationId", workstationId );
        query.setParameter( "eventStartDate", eventStartDate );
        query.setParameter( "eventEndDate", eventEndDate );
        List<ActivityUserEntry> entries = query.getResultList();

        return entries;
    }

    public long getNumberOfActivities(long id) {
        Query<Long> query = currentSession().createNamedQuery( "Process.getNumberOfActivities", Long.class );
        query.setParameter( "processId", id );
        List<Long> entries = query.getResultList();
        return entries.size();
    }

    public long getNumberOfCases(long id) {
        Query<String> query = currentSession().createNamedQuery( "Process.getNumberOfCases", String.class );
        query.setParameter( "processId", id );
        List<String> entries = query.getResultList();

        return entries.size();
    }
}
