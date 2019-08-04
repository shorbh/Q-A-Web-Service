package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {
    @Autowired
    CheckUserDao userDao;
    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.checkEmail(username);
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        }
        String encrypt = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encrypt.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encrypt);
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUuid(userEntity.getUuid());
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiredAt = now.plusHours(8);
            userAuthTokenEntity.setAccess_Token(jwtTokenProvider.generateToken(userEntity.getUuid(), now,expiredAt));
            userAuthTokenEntity.setLogin_At(now);
            userAuthTokenEntity.setExpires_At(expiredAt);
            userAuthTokenEntity.setUser_id(3);
            userDao.createAuthToken(userAuthTokenEntity);

            //userDao.updateUser(userEntity);
            return userAuthTokenEntity;
        }
        else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }
}
