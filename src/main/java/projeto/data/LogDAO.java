package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.LogDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Event;
import projeto.core.Log;

import java.util.List;

public class LogDAO extends BaseDAO<Log, LogDTO>{


    public LogDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Log toEntity(LogDTO dto) throws TransformToEntityException {

        Log log;

        try {
            log = findOrFail(dto.getId());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        // Fields allowed to change
        log.setDescription( dto.getDescription() );

        return log;
    }

    @Override
    public LogDTO toDTO(Log entity)
    {
        return new LogDTO(
                entity.getId(),
                entity.getFileName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNumberOfCases(),
                entity.getNumberOfActivities()
        );

    }

    public List<Event> getEventsByLogID( long logId ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Log.getEventsByLogID", Event.class );
        query.setParameter( "logId", logId );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Log '" + logId + "' not found." );

        return events;
    }

    public List<Long> getCaseIDsfromLogID( long logId ) throws EntityDoesNotExistException
    {
        Query<Long> query = currentSession().createNamedQuery( "Log.getCaseIDsFromLogID", Long.class );
        query.setParameter( "logId", logId );
        List<Long> cases = query.getResultList();

        if( cases.size() == 0 )
            throw new EntityDoesNotExistException( "Log '" + logId + "' not found." );

        return cases;
    }

    public List<String> getActivitiesfromLogID( long logId ) throws EntityDoesNotExistException
    {
        Query<String> query = currentSession().createNamedQuery( "Log.getActivities", String.class );
        query.setParameter( "logId", logId );
        List<String> activities = query.getResultList();

        if( activities.size() == 0 )
            throw new EntityDoesNotExistException( "Log '" + logId + "' not found." );

        return activities;
    }

    public List<String> getResourcesfromLogID( long logId ) throws EntityDoesNotExistException
    {
        Query<String> query = currentSession().createNamedQuery( "Log.getResources", String.class );
        query.setParameter( "logId", logId );
        List<String> resources = query.getResultList();

        if( resources.size() == 0 )
            throw new EntityDoesNotExistException( "Log '" + logId + "' not found." );

        return resources;
    }

    public List<Event> getEventsFromCasesfromLog(long logId, List<Long> cases ) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery( "Event.getCases", Event.class );
        query.setParameter( "logId", logId );
        query.setParameterList( "cases", cases );
        List<Event> events = query.getResultList();

        if( events.size() == 0 )
            throw new EntityDoesNotExistException( "Couldn't find the cases specified from Log '" + logId + "" );

        return events;
    }

}
