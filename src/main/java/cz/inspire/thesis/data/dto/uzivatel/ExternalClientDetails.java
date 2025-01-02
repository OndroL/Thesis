package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalClientDetails {
    private String id;
    private String name;
    private String userGroupId;
    private OAuth2ClientSettingDetails oAuth2ClientSetting;
}
