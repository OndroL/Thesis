package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/** Add DB restrictions for NULL
 * Rename Entities - e.g. remove Bean from name
 * */


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="noteheader")
public class HeaderBean {
    @Id
    private String id;
    @Column
    private int field;
    @Column
    private int location;
}
