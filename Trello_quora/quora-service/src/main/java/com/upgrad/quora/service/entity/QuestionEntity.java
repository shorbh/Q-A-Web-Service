package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "question", schema = "public")
@NamedQueries({
        @NamedQuery(name = "getUserQuestions" , query = "select ut from QuestionEntity ut "),
        @NamedQuery(name = "getQuestions" , query = "select ut from QuestionEntity ut where ut.uuid = :id "),
        @NamedQuery(name= "putUpdatedQuestion", query = "select ut from QuestionEntity ut where ut.user_id = :user_id and ut.uuid = :id"),
        @NamedQuery(name = "getUserQuestionsWithId" , query = "select ut from QuestionEntity ut where ut.user_id = :id ")
})
public class QuestionEntity {
    @Id
    @Column(name = "ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name= "CONTENT")
    private String content;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "USER_ID")
    private int user_id;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder().append(this).hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
//    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return user_id == that.user_id &&
                id.equals(that.id) &&
                uuid.equals(that.uuid) &&
                content.equals(that.content) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, content, date, user_id);
    }
}
