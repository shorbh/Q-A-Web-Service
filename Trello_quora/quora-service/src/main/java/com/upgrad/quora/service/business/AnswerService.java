package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.dao.QuestionsDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {
    @Autowired
    AnswerDao answerDao;
    @Autowired
    QuestionsDao questionsDao;
    @Autowired
    CheckUserDao userDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String answer,final String questionId,final String accessToken) throws InvalidQuestionException, AuthorizationFailedException {
            QuestionEntity question = questionsDao.getQuestion(questionId);
            if(question == null){
                throw new InvalidQuestionException("QUES-001","The question entered is invalid");
            }
            UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
            if(authTokenEntity == null){
                throw new AuthorizationFailedException("ATHR-001","User has not signed in");
            }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answer);
        answerEntity.setDate(new Date());
        answerEntity.setUser_id(authTokenEntity.getUser_id());
        answerEntity.setQuestion_id(question.getId());
        answerDao.createAnswer(answerEntity);
        return answerEntity;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void editAnswer(final String answerContent,final String answerId,final String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
        }
        AnswerEntity answerEntity = answerDao.getAnswerByAnsId(answerId);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(authTokenEntity.getUser_id() == answerEntity.getUser_id()){
            answerEntity.setAns(answerContent);
            answerDao.editAnswer(answerEntity);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }

        }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(final String answerId,final String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }
        AnswerEntity answerEntity = answerDao.getAnswerByAnsId(answerId);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        UserEntity userEntity = userDao.getUserByUuid(authTokenEntity.getUuid());
        if(authTokenEntity.getUser_id() == answerEntity.getUser_id() || (userEntity.getRole()).compareTo("admin") == 0){
            answerDao.deleteAnswer(answerEntity);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
        }

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId,final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get the answers");
        }
        QuestionEntity question = questionsDao.getQuestion(questionId);
        if(question == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        List<AnswerEntity> allAnswersToQuestion = answerDao.getAllAnswersToQuestion(question.getId());
        for(AnswerEntity a : allAnswersToQuestion)
        a.setQuestionContent(question.getContent());
        return allAnswersToQuestion;
    }

    }
