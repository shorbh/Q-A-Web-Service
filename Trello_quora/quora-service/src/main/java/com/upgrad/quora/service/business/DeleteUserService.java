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
public class DeleteUserService {
    @Autowired
    private CheckUserDao userDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String uuid,final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(authorization);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if((authTokenEntity.getExpires_At()).compareTo(ZonedDateTime.now()) < 0){
            throw new AuthorizationFailedException("ATHR-002","User is signed out");
        }
        UserEntity userEntity = userDao.getUserByUuid(authTokenEntity.getUuid());
        if((userEntity.getRole()).compareTo("nonadmin") == 0){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }
        UserEntity entity = userDao.getUserByUuid(uuid);
        if(entity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        userDao.deleteUser(entity);
        return uuid;
    }
}
