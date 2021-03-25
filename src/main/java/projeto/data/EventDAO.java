package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import projeto.api.dtos.entities.*;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Activity;
import projeto.core.Event;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Process;

import java.util.List;

public class EventDAO extends BaseDAO<Event, EventDTO>
{

    public EventDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public Event toEntity(EventDTO dto) throws TransformToEntityException, EntityDoesNotExistException
    {
        Event event;

        try {
            event = findOrFail(dto.getId());
        } catch (EntityDoesNotExistException e) {
            throw new TransformToEntityException(e.getMessage());
        }

        // Fields allowed to change
        if (dto.getEndDate() != null) {
            event.setEndDate(dto.getEndDate());
        }

        return event;
    }


    @Override
    public EventDTO toDTO(Event entity)
    {
        if (entity.getPart() != null) {
            return new EventDTO(
                    entity.getActivity().getId(),
                    entity.getProcess().getId(),
                    entity.getMould().getCode(),
                    entity.getPart().getCode(),
                    entity.getStartDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    entity.getEndDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    entity.getDuration(),
                    entity.getIsEstimatedEnd(),
                    entity.getId()
            );
        }
        return new EventDTO(
                entity.getActivity().getId(),
                entity.getProcess().getId(),
                entity.getMould().getCode(),
                entity.getStartDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                entity.getEndDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                entity.getDuration(),
                entity.getIsEstimatedEnd(),
                entity.getId()
        );
    }

    //DTO que contém os objetos mould e part (VAAM)
    public EventDTO toFullDTO(Event event)
    {
        if (event.getPart() != null) {
            return new EventDTO(
                    activityToDTO(event.getActivity()),
                    processToDTO(event.getProcess()),
                    mouldToDTO(event.getMould()),
                    partToDTO(event.getPart()),
                    event.getStartDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    event.getEndDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                    event.getDuration(),
                    event.getIsEstimatedEnd(),
                    event.getId()
            );
        }
        return new EventDTO(
                activityToDTO(event.getActivity()),
                processToDTO(event.getProcess()),
                mouldToDTO(event.getMould()),
                null,
                event.getStartDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                event.getEndDate().toString("dd-MM-yyyy HH:mm:ss.SSS"),
                event.getDuration(),
                event.getIsEstimatedEnd(),
                event.getId()
        );
    }

    //para formatar o fullDTO
    public ActivityDTO activityToDTO(Activity activity)
    {
        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription()
        );
    }

    //para formatar o fullDTO
    public ProcessDTO processToDTO(Process process)
    {
        return new ProcessDTO(
                process.getId(),
                process.getName(),
                process.getDescription(),
                process.getStartDate(),
                process.getEndDate(),
                process.getNumberOfCases(),
                process.getNumberOfActivities()
        );
    }

    //para formatar o fullDTO
    public MouldDTO mouldToDTO(Mould mould)
    {
        return new MouldDTO(
                mould.getCode(),
                mould.getDescription()
        );
    }

    //para formatar o fullDTO
    public PartDTO partToDTO(Part part)
    {
        if (part == null)
            return null;
        if (part.getTag() != null) {
            return new PartDTO(
                    part.getCode(),
                    part.getDescription(),
                    part.getTag().toString()
            );
        }
        return new PartDTO(
                part.getCode(),
                part.getDescription(),
                null
        );
    }

    public List<Event> getEventByMouldCode(String code) throws EntityDoesNotExistException
    {
        Query<Event> query = currentSession().createNamedQuery("Event.getEventsByMouldCode", Event.class);
        query.setParameter("mouldCode", code);
        List<Event> events = query.getResultList();

        if (events.size() < 1)
            throw new EntityDoesNotExistException("Não existem eventos associados ao molde: " + code + ".");

        return events;
    }

    public Double getAverageEventDurationByActivity(long activityId)
            throws EntityDoesNotExistException
    {
        Query<Double> query = currentSession().createNamedQuery("Event.getAverageEventDurationByActivity",Double.class);
        query.setParameter("activityId", activityId);
        List<Double> avgDuration = query.getResultList();

        if (avgDuration.size()<1)
            throw new EntityDoesNotExistException("A Activity com id: "+activityId+" não está associada a nenhum evento");

        if (avgDuration.size()>1)
            throw new EntityDoesNotExistException("Erro? query devolveu mais do que um valor?");

        System.out.println("######################################");
        for (Double a : avgDuration)
        {
            System.out.println(a);
        }
        System.out.println("######################################");

        return avgDuration.get(0);
    }
}
