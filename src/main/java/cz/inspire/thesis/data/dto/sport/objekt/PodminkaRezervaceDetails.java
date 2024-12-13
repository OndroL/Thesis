package cz.inspire.thesis.data.dto.sport.objekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PodminkaRezervaceDetails {
    private String id;
    private String name;
    private long priorita;
    private String objektRezervaceId;
    private Boolean objektRezervaceObsazen;
    private String objektId;
}