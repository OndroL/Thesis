package cz.inspire.thesis.data.dto.sport.sport;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstructorDetails {
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
    private Set<ActivityDetails> activities;
    private Set<SportDetails> sports;

    /**
     * Added for ActivityBean getDetails functionality
     */
    public InstructorDetails(String id, String firstName, String lastName, String color) {
    }
}
