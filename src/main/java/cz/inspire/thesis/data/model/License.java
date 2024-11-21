package cz.inspire.thesis.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="license")
public class License {
    @Id
    private String id;
    @Column
    private String customer;
    @Column
    private Boolean valid;
    @Column
    private Boolean centeronline;
    @Column
    private Date validfrom;
    @Column
    private Boolean validfromset;
    @Column
    private Date validto;
    @Column
    private int activitylimit;
    @Column
    private int sportcenterlimit;
    @Column
    private int sportcustomerslimit;
    @Column
    private int userslimit;
    @Column
    private int customergroupslimit;
    @Column
    private int pokladnalimit;
    @Column
    private int skladlimit;
    @Column
    private int maxclients;
    @Column
    private Boolean ovladaniquido;
    @Column
    private Long modules;
    @Column
    private String hash;
    @Column
    private Date createddate;
    @Column
    private Date generateddate;
    @Column
    private int centerid;
}
