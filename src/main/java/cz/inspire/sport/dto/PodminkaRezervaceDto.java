package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PodminkaRezervaceDto implements Serializable {
    private String id;
    private String name;
    private long priorita;
    private String objektRezervaceId;
    private Boolean objektRezervaceObsazen;
    private String objektId;
}