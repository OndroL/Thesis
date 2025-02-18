package cz.inspire.license.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="license")
public class LicenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String customer;

    @Column
    private Boolean valid;

    @Column
    private Boolean centerOnline;

    @Column
    private Date validFrom;

    @Column
    private Boolean validFromSet;

    @Column
    private Date validTo;

    @Column
    private Boolean validToSet;

    @Column
    private Integer activityLimit;

    @Column
    private Integer sportCenterLimit;

    @Column
    private Integer sportCustomersLimit;

    @Column
    private Integer usersLimit;

    @Column
    private Integer customerGroupsLimit;

    @Column
    private Integer pokladnaLimit;

    @Column
    private Integer skladLimit;

    @Column
    private Integer maxClients;

    @Column
    private Boolean ovladaniQuido;

    @Column
    private Long modules;

    @Column
    private String hash;

    @Column
    private Date createdDate;

    @Column
    private Date generatedDate;

    @Column
    private int centerId;
}
