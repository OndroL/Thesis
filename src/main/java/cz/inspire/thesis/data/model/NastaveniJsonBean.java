package cz.inspire.thesis.data.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name="nastaveni_json")
public class NastaveniJsonBean {
    @Id
    private String key;
    @Column
    private String value;
}
