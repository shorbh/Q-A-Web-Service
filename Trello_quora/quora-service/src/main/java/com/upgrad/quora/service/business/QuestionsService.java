package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.dao.QuestionsDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionsService {
    @Autowired
    private QuestionsDao questionsDao;
    @Autowired
    private CheckUserDao userDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public String createQuestion(final QuestionEntity questionEntity,final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }
        UserEntity userEntity = userDao.getUserByUuid(authTokenEntity.getUuid());
        questionEntity.setUuid(UUID.randomUUID().toString());  //1 change
        questionEntity.setUser_id(userEntity.getId());
        questionEntity.setDate(new Date());
        questionsDao.createQuestion(questionEntity);
        return questionEntity.getUuid();
    }
    public List<QuestionEntity> getAllQuestions(final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }
        return questionsDao.getAllQuestions();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void editQuestion(final String content,final String id,final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
        }
        QuestionEntity questionEntity = questionsDao.updateQuestion(authTokenEntity.getUser_id(), id);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","Only the question owner can edit the question");
        }
        questionEntity.setContent(content);
        questionsDao.createQuestion(questionEntity);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(final String id,final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
        }
        UserEntity userEntity = userDao.getUserByUuid(authTokenEntity.getUuid());
        QuestionEntity questionEntity = questionsDao.getQuestionById(id);
        if(questionEntity.getUser_id() == authTokenEntity.getUser_id() || (userEntity.getRole()).compareTo("admin") ==0){
            questionsDao.deleteQuestion(questionEntity);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question");
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getUserQuestions(final String uuid,final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }
        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
        return questionsDao.getUserQuestions(userEntity.getId());
    }
}
