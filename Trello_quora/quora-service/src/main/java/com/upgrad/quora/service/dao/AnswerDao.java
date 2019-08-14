package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;
    public void createAnswer(final AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
    }
    public AnswerEntity getAnswerByAnsId(final String answerId){
        try{
            return entityManager.createNamedQuery("getAnswerByAnsId",AnswerEntity.class).setParameter("answerId",answerId).getSingleResult();
        }
        catch (Exception e){
            return  null;
        }
    }
    public void editAnswer(final AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
    }
    public void deleteAnswer(AnswerEntity answerEntity){
        entityManager.remove(answerEntity);
    }
    public List<AnswerEntity> getAllAnswersToQuestion(final int questionId){
        return entityManager.createNamedQuery("getAllAnswerByQuestionId",AnswerEntity.class).setParameter("question_id",questionId).getResultList();
    }
}
