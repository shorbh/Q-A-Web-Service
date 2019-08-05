package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserDetailsService {
    @Autowired
    private CheckUserDao userDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(final String uuid,final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(authorization);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getUuid()).compareTo(uuid) != 0){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        return userDao.getUserByUuid(uuid);
    }
}
