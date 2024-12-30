package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "external_client")
public class ExternalClientEntity {
    @Id
    private String id;
    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_group", referencedColumnName = "id")
    private SkupinaEntity userGroup;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "oauth2_setting_id", referencedColumnName = "id")
    private OAuth2ClientSettingEntity oauth2ClientSetting;
}
