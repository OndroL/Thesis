package cz.inspire.thesis.data.model.sport;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "omezeni_rezervaci")
public class OmezeniRezervaciEntity implements Serializable {

    @Id
    @Column(name = "objekt_id", nullable = false)
    private String objektId;

    @Lob
    @Column(name = "omezeni", nullable = false)
    private OtviraciDoba omezeni;
}

