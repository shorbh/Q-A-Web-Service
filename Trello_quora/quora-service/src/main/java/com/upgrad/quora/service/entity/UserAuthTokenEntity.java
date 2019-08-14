package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth", schema = "public")
@NamedQueries({
        @NamedQuery(name = "userByAccessToken" , query = "select ut from UserAuthTokenEntity ut where ut.access_Token = :accessToken "),
        @NamedQuery(name = "userById" , query = "select ut from UserAuthTokenEntity ut where ut.user_id = :id ")
})
public class UserAuthTokenEntity implements Serializable {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@ManyToOne
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "USER_ID")
    private int user_id;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String access_Token;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime login_At;

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expires_At;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logout_At;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccess_Token() {
        return access_Token;
    }

    public void setAccess_Token(String access_Token) {
        this.access_Token = access_Token;
    }

    public ZonedDateTime getLogin_At() {
        return login_At;
    }

    public void setLogin_At(ZonedDateTime login_At) {
        this.login_At = login_At;
    }

    public ZonedDateTime getExpires_At() {
        return expires_At;
    }

    public void setExpires_At(ZonedDateTime expires_At) {
        this.expires_At = expires_At;
    }

    public ZonedDateTime getLogout_At() {
        return logout_At;
    }

    public void setLogout_At(ZonedDateTime logout_At) {
        this.logout_At = logout_At;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
