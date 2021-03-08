package projeto.api.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

@NoArgsConstructor
@Getter @Setter
public class DurationDTO
{
    private long days;
    private long hours;
    private long minutes;
    private long seconds;
    private long millis;

    public DurationDTO( Duration duration ) { this( duration.getMillis() ); }

    public DurationDTO( long millis )
    {
        if( millis < 0 )
            throw new IllegalArgumentException( "Millis cant be negative : " + millis );
        //if( millis == 0 )
        //    return;

        if( millis >= DateTimeConstants.MILLIS_PER_DAY )
        {
            days = millis / DateTimeConstants.MILLIS_PER_DAY;
            millis -= days * DateTimeConstants.MILLIS_PER_DAY;
        }

        if( millis >= DateTimeConstants.MILLIS_PER_HOUR )
        {
            hours = millis / DateTimeConstants.MILLIS_PER_HOUR;
            millis -= hours * DateTimeConstants.MILLIS_PER_HOUR;
        }

        if( millis >= DateTimeConstants.MILLIS_PER_MINUTE )
        {
            minutes = millis / DateTimeConstants.MILLIS_PER_MINUTE;
            millis -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
        }

        if( millis >= DateTimeConstants.MILLIS_PER_SECOND )
        {
            seconds = millis / DateTimeConstants.MILLIS_PER_SECOND;
            millis -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
        }

        this.millis = millis;

    }

    public long toMillis()
    {
        return toMillis( this.days, this.minutes, this.hours, this.seconds, this.millis );
    }

    private long toMillis( long days, long hours, long minutes, long seconds, long millis )
    {
        return
                DateTimeConstants.MILLIS_PER_DAY * days +
                        DateTimeConstants.MILLIS_PER_HOUR * hours +
                        DateTimeConstants.MILLIS_PER_MINUTE * minutes +
                        DateTimeConstants.MILLIS_PER_SECOND * seconds +
                        millis;
    }

    /*
    public long minus( DurationDTO duration )
    {
        long days, hours, minutes, seconds, millis;
        days = this.days - duration.days;
        hours = this.hours - duration.hours;
        minutes = this.minutes - duration.minutes;
        seconds = this.seconds - duration.seconds;
        millis = this.millis - duration.millis;

        return toMillis( days, hours, minutes, seconds, millis );

    }
    */

}
