package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class InstructorDto implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private int index;
    private String email;
    private String phoneCode;
    private String phoneNumber;
    private String emailInternal;
    private String phoneCodeInternal;
    private String phoneNumberInternal;
    private String info;
    private String color;
    private byte[] photo;
    private Boolean deleted;
    private String googleCalendarId;
    private Boolean googleCalendarNotification;
    private int googleCalendarNotificationBefore;
    private Set<ActivityDto> activities;
    private Set<SportDto> sports;
}
