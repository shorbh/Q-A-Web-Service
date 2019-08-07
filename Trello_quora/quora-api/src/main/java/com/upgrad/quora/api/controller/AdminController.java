package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.DeleteUserService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
    @Autowired
    DeleteUserService deleteUserService;
    @RequestMapping(method = RequestMethod.DELETE,path = "/admin/user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String uuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String s = deleteUserService.deleteUser(uuid, authorization);
        UserDeleteResponse deleteResponse = new UserDeleteResponse().id(s).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(deleteResponse, HttpStatus.OK);
    }
}
