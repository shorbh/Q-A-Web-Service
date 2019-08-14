package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionsService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    QuestionsService questionsService;
    @RequestMapping(method = RequestMethod.POST,path ="/question/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        String uuid = questionsService.createQuestion(questionEntity,accessToken);
        QuestionResponse questionResponse = new QuestionResponse().id(uuid).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.GET,path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, IOException {
        List<QuestionEntity> list = questionsService.getAllQuestions(accessToken);
        String uuid = list.get(0).getUuid();

        List<QuestionDetailsResponse> li = new ArrayList<>();

        String content = "";
        for(QuestionEntity q : list){
            li.add(new QuestionDetailsResponse().id(q.getUuid()).content(q.getContent()));
        }

        //QuestionDetailsResponse questionDetailsResponse = new ObjectMapper().readValue(response, QuestionDetailsResponse.class);
        return new ResponseEntity<List<QuestionDetailsResponse>>(li, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.PUT,path = "/question/edit/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(final QuestionEditRequest editRequest, @PathVariable("questionId") String questionId,@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        questionsService.editQuestion(editRequest.getContent(),questionId,accessToken);  //change
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionId).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.DELETE,path = "/question/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") String questionId,@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        questionsService.deleteQuestion(questionId,accessToken); //change
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET,path = "question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getUserQuesions(@PathVariable("userId") final String userId,@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> entities = questionsService.getUserQuestions(userId, accessToken);
        List<QuestionDetailsResponse> li= new ArrayList<>();
        for(QuestionEntity e : entities){
            li.add(new QuestionDetailsResponse().id(e.getUuid()).content(e.getContent()));
        }


        return new ResponseEntity<List<QuestionDetailsResponse>>(li,HttpStatus.OK);
    }
}
