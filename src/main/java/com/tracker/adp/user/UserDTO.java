package com.tracker.adp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tracker.adp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty(message = "Please enter valid name.")
    private String name;

    @NotEmpty(message = "Please enter valid email.")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Please enter valid mobile.")
    private String mobile;

    @NotEmpty(message = "Please enter valid password.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Role role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordResetToken;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String avatar;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String personalMeetingID;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String passCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String invitationLink;

    private String education="";
    private String organization="";
    private String address="";
    private String occupation="";
    private String designation="";


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String jwt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int adminApproveStatus;

}
