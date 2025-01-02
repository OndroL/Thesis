package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2ClientSettingDetails {
    private String id;
    private String clientId;
    private String clientSecret;
    private String scopes;
    private String resourceIds;
    private String authorizedGrantTypes;
    private String registeredRedirectUris;
    private String autoApproveScopes;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
}
