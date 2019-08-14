package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionsDao {
    @PersistenceContext
    private EntityManager entityManager;
    public void createQuestion(final QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
    }
    public List<QuestionEntity> getAllQuestions(){
        return entityManager.createNamedQuery("getUserQuestions",QuestionEntity.class).getResultList();
    }
    public QuestionEntity updateQuestion(final int user_id,final String id){
        try {
            try {
                entityManager.createNamedQuery("getQuestions", QuestionEntity.class).setParameter("id", id).getSingleResult();
            }
            catch(Exception e){
                throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
            }
            return entityManager.createNamedQuery("putUpdatedQuestion", QuestionEntity.class).setParameter("user_id", user_id).setParameter("id", id).getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }
    public QuestionEntity getQuestionById(final String id) throws InvalidQuestionException {
        try {
             return entityManager.createNamedQuery("getQuestions", QuestionEntity.class).setParameter("id", id).getSingleResult();
        }
        catch(Exception e){
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }
    }
    public void deleteQuestion(QuestionEntity questionEntity){
        entityManager.remove(questionEntity);
    }
    public List<QuestionEntity> getUserQuestions(final int id){

            return entityManager.createNamedQuery("getUserQuestionsWithId",QuestionEntity.class).setParameter("id",id).getResultList();
    }
    public QuestionEntity getQuestion(final String questionId){
        try {
            return entityManager.createNamedQuery("getQuestions",QuestionEntity.class).setParameter("id",questionId).getSingleResult();
        }
        catch (Exception e){
            return null;
        }

    }
}
