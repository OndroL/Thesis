package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
/** Add DB restrictions for NULL */
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="nastaveni")
public class NastaveniBean {
    @Id
    private String key;
    @Column
    private Serializable value;
}
