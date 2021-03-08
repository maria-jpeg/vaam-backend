package projeto.controller;

import org.joda.time.DateTime;
import projeto.api.dtos.entities.EventDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Event;
import projeto.data.EventDAO;
import projeto.resources.EventServ;

import java.util.logging.Logger;

public class EventBean extends BaseBean<Event, EventDTO> {
    private static final Logger LOGGER = Logger.getLogger(EventServ.class.getName());

    final private EventDAO eventDAO;

    public EventBean(EventDAO eventDAO) {
        super(eventDAO);
        this.eventDAO = eventDAO;
    }

    public EventDTO toDTO(Event entity){
        return eventDAO.toDTO(entity);
    }

    /*@Override
    public EventDTO update(EventDTO eventDTO) throws EntityDoesNotExistException {
        Event event = eventDAO.findOrFail(eventDTO.getId());

        if(event != null) {
            event.setCaseId(eventDTO.getCaseId());
            event.setName(eventDTO.getName());
            event.setResource(eventDTO.getResource());
            event.setRole(eventDTO.getRole());
            event.setIsEstimatedEnd(eventDTO.getIsEstimatedEnd());

            if(eventDTO.getStartDate() != null){ //se o startDate não vier preenchido no update, deixamos como estava (para não ficar a null)
                event.setStartDate(eventDTO.getStartDate());
            }
            if(eventDTO.getEndDate() != null){
                event.setEndDate(eventDTO.getEndDate());
            }

            if(event.getStartDate() != null && event.getEndDate() != null){ //se ambos start e end Date já estiverem preenchidos, vamos calcular a duração
                event.setDuration(event.getEndDate().getMillis() - event.getStartDate().getMillis());
            }
        }

        return toDTO(event);
    }*/

}
