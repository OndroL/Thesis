package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "oauth2_client_setting")
public class OAuth2ClientSettingEntity {

    @Id
    private String id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column
    private String scopes;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "registered_redirect_uris")
    private String registeredRedirectUris;

    @Column(name = "auto_approve_scopes")
    private String autoApproveScopes;

    @Column(name = "access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;
}
