package com.tracker.adp.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tracker.adp.core.BaseEntity;
import com.tracker.adp.enums.Gender;
import com.tracker.adp.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails, CredentialsContainer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String mobile;

    @JsonIgnore
    @Column(name = "reset_token")
    private String passwordResetToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "education", columnDefinition = "varchar(255) default ''")
    private String education;

    @Column(name="organization", columnDefinition = "varchar(255) default ''")
    private String organization;

    @Column(name="address", columnDefinition = "varchar(255) default ''")
    private String address;

    @Column(name="occupation", columnDefinition = "varchar(255) default ''")
    private String occupation;

    @Column(name="designation", columnDefinition = "varchar(255) default ''")
    private String designation;

    @Column(name="avatar_id")
    private String avatarID;

    @Column(name="personal_meeting_id", unique = true)
    private String personalMeetingID;

    @Column(name="meeting_passcode")
    private String passCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "role")
    private Role role;


    @Column(name="account_non_expired")
    private boolean accountNonExpired;

    @Column(name="account_non_locked")
    private boolean accountNonLocked;

    @Column(name="credentials_non_expired")
    private boolean credentialsNonExpired;

    private boolean enabled;

    @Column(name="admin_approve_status", columnDefinition = "integer default 0")
    private int adminApproveStatus;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role)
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public User(String name, String email, String mobile, String password, Role role) {
        this.name = name;
        this.email = email;
        this.mobile= mobile;
        this.password = password;
        this.role = role;
    }
}
