package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ACTIVITIES_USERS")//( au.startDate >= :eventStartDate AND au.startDate <= :eventEndDate) AND ( au.endDate >= :eventStartDate AND au.endDate <= :eventEndDate)
@NamedQueries({                  //antigo errado -> ( au.startDate <= :eventEndDate AND au.endDate >= :eventStartDate)
        @NamedQuery(name = "ActivitiesUsers.getEntriesAssociatedToEventActivity",//correção query VAAM? ^
                query = "SELECT au FROM ActivityUserEntry au WHERE au.activity.id = :activityId AND ( ( au.startDate >= :eventStartDate AND au.startDate <= :eventEndDate) AND ( au.endDate >= :eventStartDate AND au.endDate <= :eventEndDate) )"),
        @NamedQuery(name = "ActivitiesUsers.getEntriesAssociatedToEventWorkstation",
                query = "SELECT au FROM ActivityUserEntry au WHERE au.workstation.id = :workstationId AND ( ( au.startDate <= :eventEndDate AND au.endDate >= :eventStartDate) )"),
        @NamedQuery(name = "ActivitiesUsers.getEntriesAssociatedToUsername",
                query = "SELECT au FROM ActivityUserEntry au WHERE au.user.username = :username AND ( ( au.startDate <= :eventEndDate AND au.endDate >= :eventStartDate) )")
})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityUserEntry implements Serializable {
    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @MapsId("activityId")
    @JoinColumn(name = "activityId")
    private Activity activity;

    @Id
    private DateTime startDate;

    private DateTime endDate;

    @ManyToOne
    @JoinColumn(name = "workstationId")
    private Workstation workstation;

}
