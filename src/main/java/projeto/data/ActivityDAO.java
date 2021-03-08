package projeto.data;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import projeto.api.dtos.entities.ActivityDTO;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Activity;
import projeto.core.ActivityUserEntry;

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

    public List<ActivityUserEntry> getEntriesAssociatedToEventActivity(long activityId, DateTime eventStartDate, DateTime eventEndDate)
    {
        Query<ActivityUserEntry> query = currentSession().createNamedQuery( "ActivitiesUsers.getEntriesAssociatedToEventActivity", ActivityUserEntry.class );
        query.setParameter( "activityId", activityId );
        query.setParameter( "eventStartDate", eventStartDate );
        query.setParameter( "eventEndDate", eventEndDate );
        List<ActivityUserEntry> entries = query.getResultList();

        return entries;
    }

}
