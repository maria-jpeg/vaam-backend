package projeto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "TAGS")
@NamedQueries({
        @NamedQuery(name = "Tag.all",
                query = "SELECT t FROM Tag t ORDER BY t.rfid"),
        @NamedQuery(
                name = "Tag.getTagsAvailableOrUser",
                query = "SELECT t FROM Tag t WHERE t.isAvailable = true OR t.isUser = true"
        )
})
@NoArgsConstructor
@Getter
@Setter
public class Tag implements Serializable {

    @Id
    private String rfid;
    private Boolean isAvailable;
    private Boolean isUser;

    @OneToOne(mappedBy = "tag")
    private Part part;

    public Tag(String rfid, Boolean isAvailable) {
        this.rfid = rfid;
        this.isAvailable = isAvailable;
    }

}
