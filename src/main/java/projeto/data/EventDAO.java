package projeto.data;

import org.hibernate.SessionFactory;
import projeto.api.dtos.entities.EventDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Event;

public class EventDAO extends BaseDAO<Event, EventDTO> {

    public EventDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Event toEntity(EventDTO dto) throws TransformToEntityException, EntityDoesNotExistException {
        Event event;

        try {
            event = findOrFail(dto.getId());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        // Fields allowed to change
        if(dto.getEndDate() != null){
            event.setEndDate( dto.getEndDate() );
        }

        return event;
    }


    @Override
    public EventDTO toDTO(Event entity)
    {
        if(entity.getPart() != null){
            return new EventDTO(
                    entity.getActivity().getId(),
                    entity.getProcess().getId(),
                    entity.getMould().getCode(),
                    entity.getPart().getCode(),
                    entity.getStartDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" ),
                    entity.getEndDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" ),
                    entity.getDuration(),
                    entity.getIsEstimatedEnd(),
                    entity.getId()
            );
        }
        return new EventDTO(
                entity.getActivity().getId(),
                entity.getProcess().getId(),
                entity.getMould().getCode(),
                entity.getStartDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" ),
                entity.getEndDate().toString( "dd-MM-yyyy HH:mm:ss.SSS" ),
                entity.getDuration(),
                entity.getIsEstimatedEnd(),
                entity.getId()
        );

    }
}
