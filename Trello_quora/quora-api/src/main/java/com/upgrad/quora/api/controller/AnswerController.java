package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AnswerController {
    @Autowired
    AnswerService answerService;
    @RequestMapping(method = RequestMethod.POST,path = "/question/{questionId}/answer/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest,@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = answerService.createAnswer(answerRequest.getAnswer(), questionId, accessToken);
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.PUT,path = "/answer/edit/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest,@PathVariable("answerId") final String answerId,@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {
        answerService.editAnswer(answerEditRequest.getContent(),answerId,accessToken);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerId).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.DELETE,path = "/answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId,@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {
        answerService.deleteAnswer(answerId,accessToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET,path = "answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@PathVariable("questionId") final String questionId,@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        List<AnswerEntity> answerEntities =  answerService.getAllAnswersToQuestion(questionId,accessToken);
        List<AnswerDetailsResponse> li = new ArrayList<>();
        for(AnswerEntity a : answerEntities){
            li.add(new AnswerDetailsResponse().id(a.getUuid()).questionContent(a.getQuestionContent()).answerContent(a.getAns()));
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(li,HttpStatus.OK);
    }
}
