package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import projeto.api.dtos.entities.ActivityDTO;
import projeto.api.dtos.entities.ActivityUserEntryDTO;
import projeto.api.dtos.entities.UserDTO;
import projeto.api.dtos.entities.WorkstationDTO;
import projeto.api.dtos.resources.workhours.ActivityUsersDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ActivityDAO extends BaseDAO<Activity, ActivityDTO> {
    public ActivityDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Activity toEntity(ActivityDTO dto) throws TransformToEntityException {
        Activity activity;

        try {
            activity = findOrFail(dto.getId());
        } catch ( EntityDoesNotExistException e )
        {
            throw new TransformToEntityException( e.getMessage() );
        }

        return activity;
    }

    @Override
    public ActivityDTO toDTO(Activity entity) {
        return new ActivityDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public ActivityUserEntryDTO AUtoDTO(ActivityUserEntry entry)
    {
        if (entry.getWorkstation() == null){
            return new ActivityUserEntryDTO(
                    UserToDTO(entry.getUser()),
                    toDTO(entry.getActivity()),
                    entry.getStartDate(),
                    entry.getEndDate(),
                    null
            );
        }
        return new ActivityUserEntryDTO(
                UserToDTO(entry.getUser()),
                toDTO(entry.getActivity()),
                entry.getStartDate(),
                entry.getEndDate(),
                WorkstationToDTO(entry.getWorkstation())
        );
    }

    public UserDTO UserToDTO(User user){
        return new UserDTO(
                user.getUsername(),
                user.getRole().getName(),
                user.getName(),
                user.getEmail()
        );
    }

    public WorkstationDTO WorkstationToDTO(Workstation workstation)
    {
        return new WorkstationDTO(
                workstation.getId(),
                workstation.getName(),
                workstation.getIsTagging(),
                workstation.getIsEndWorkstation()
        );
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

    public List<Activity> getActivityByMouldCode(String code) throws EntityDoesNotExistException
    {
        Query<Activity> query = currentSession().createNamedQuery("Activities.getActivitiesFromMouldCode",Activity.class);
        query.setParameter("mouldCode",code);
        List<Activity> activities = query.getResultList();
        if (activities.size() < 1)
            throw new EntityDoesNotExistException("Não existem atividades associadas ao molde: "+ code+".");

        return activities;
    }
}
