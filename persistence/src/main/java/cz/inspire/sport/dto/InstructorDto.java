package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
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
    private Set<ActivityDto> activities = new HashSet<>();
    private Set<SportDto> sports = new HashSet<>();
}
