package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PodminkaRezervaceDto implements Serializable {
    private String id;
    private String name;
    private long priorita;
    private String objektRezervaceId;
    private Boolean objektRezervaceObsazen;
    private String objektId;
}