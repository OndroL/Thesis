package cz.inspire.sport.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String objektId;

    private LocalDateTime platnostOd;
}
