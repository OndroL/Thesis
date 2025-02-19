package cz.inspire.sport.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OtviraciDobaObjektuPK implements Serializable {
    private String objektId;

    private LocalDateTime platnostOd;
}
