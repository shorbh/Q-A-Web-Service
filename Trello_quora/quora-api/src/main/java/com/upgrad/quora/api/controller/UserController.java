package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private SignupBusinessService businessService;
    @RequestMapping(method = RequestMethod.POST,path="/user/signup",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest)throws SignUpRestrictedException {
        UserEntity entity = null;
        if(businessService.checkUserName(signupUserRequest.getUserName()) && businessService.checkEmail(signupUserRequest.getEmailAddress())){
            final UserEntity userEntity = new UserEntity();
            userEntity.setUuid(UUID.randomUUID().toString());
            userEntity.setFirstName(signupUserRequest.getFirstName());
            userEntity.setLastName(signupUserRequest.getLastName());
            userEntity.setUsername(signupUserRequest.getUserName());
            userEntity.setEmail(signupUserRequest.getEmailAddress());
            userEntity.setPassword(signupUserRequest.getPassword());
            userEntity.setContactnumber(signupUserRequest.getContactNumber());
            userEntity.setDob(signupUserRequest.getDob());
            userEntity.setAboutme(signupUserRequest.getAboutMe());
            userEntity.setRole("nonadmin");
            userEntity.setCountry(signupUserRequest.getCountry());
            entity = businessService.signup(userEntity);

        }
        SignupUserResponse userResponse = new SignupUserResponse().id(entity.getUuid()).status("REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
    }
}
