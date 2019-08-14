package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "answer", schema = "public")
@NamedQueries({
         @NamedQuery(name = "getAnswerByAnsId" , query = "select ut from AnswerEntity ut where ut.uuid = :answerId "),
        @NamedQuery(name = "getAllAnswerByQuestionId" , query = "select ut from AnswerEntity ut where ut.question_id = :question_id")
})
public class AnswerEntity {
    @Id
    @Column(name = "ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name= "ANS")
    private String ans;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "USER_ID")
    private int user_id;

    @Column(name = "QUESTION_ID")
    private int question_id;

    @Transient
    private String questionContent;

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", ans='" + ans + '\'' +
                ", date=" + date +
                ", user_id=" + user_id +
                ", question_id=" + question_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerEntity that = (AnswerEntity) o;
        return user_id == that.user_id &&
                question_id == that.question_id &&
                id.equals(that.id) &&
                uuid.equals(that.uuid) &&
                ans.equals(that.ans) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, ans, date, user_id, question_id);
    }
}
