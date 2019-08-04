package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByName", query = "select u from UserEntity u where u.username =:username")
        }
)


public class UserEntity implements Serializable{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 200)
    private String email;

   // @ToStringExclude
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 200)
    private String firstname;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 200)
    private String lastname;

    @Column(name = "CONTACTNUMBER")
    @NotNull
    @Size(max = 50)
    private String contactnumber;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "ABOUTME")
    private String aboutme;
    @Column(name="COUNTRY")
    private String country;
//    @Column(name = "STATUS")
//    @NotNull
//    private int status;
//
//    @Column(name = "FAILED_LOGIN_COUNT")
//    @Min(0)
//    @Max(5)
//    private int failedLoginCount;
//
//    @Column(name = "LAST_PASSWORD_CHANGE_AT")
//    private ZonedDateTime lastPasswordChangeAt;
//
//    @Column(name = "LAST_LOGIN_AT")
//    private ZonedDateTime lastLoginAt;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;

//    @Version
//    @Column(name="VERSION" , length=19 , nullable = false)
//    private Long version;

//
//    @Column(name="CREATED_BY")
//    @NotNull
//    private String createdBy;
//
//
//    @Column(name="CREATED_AT")
//    @NotNull
//    private ZonedDateTime createdAt;
//
//    @Column(name="MODIFIED_BY")
//    private String modifiedBy;
//
//    @Column(name="MODIFIED_AT")
//    private ZonedDateTime modifiedAt;


    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

//    public Long getVersion() {
//        return version;
//    }
//

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
//    public void setVersion(Long version) {
//        this.version = version;
//    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}