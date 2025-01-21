package cz.inspire.license.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseDto implements Serializable {
    private String id;
    private int centerId;
    private String customer;
    private Boolean valid;
    private Boolean centerOnline;
    private Boolean validFromSet;
    private Date validFrom;
    private Boolean validToSet;
    private Date validTo;
    private Integer activityLimit;
    private Integer sportCenterLimit;
    private Integer sportCustomersLimit;
    private Integer usersLimit;
    private Integer customerGroupsLimit;
    private Integer pokladnaLimit;
    private Integer skladLimit;
    private Boolean ovladaniQuido;
    private Long modules;
    private Integer maxClients;
    private String hash;
    private Date createdDate;
    private Date generatedDate;
}
