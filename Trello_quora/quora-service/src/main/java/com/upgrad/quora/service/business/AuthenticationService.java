package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
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
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiredAt = now.plusHours(8);
            UserAuthTokenEntity entity = userDao.getUser(userEntity.getId());
            if(entity!=null){
                entity.setAccess_Token(jwtTokenProvider.generateToken(userEntity.getUuid(), now,expiredAt));
                entity.setLogin_At(now);
                entity.setExpires_At(expiredAt);
                return userDao.createAuthToken(entity);
            }
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUuid(userEntity.getUuid());
            userAuthTokenEntity.setAccess_Token(jwtTokenProvider.generateToken(userEntity.getUuid(), now,expiredAt));
            userAuthTokenEntity.setLogin_At(now);
            userAuthTokenEntity.setExpires_At(expiredAt);
            userAuthTokenEntity.setUser_id(userEntity.getId());
            userDao.createAuthToken(userAuthTokenEntity);

            //userDao.updateUser(userEntity);
            return userAuthTokenEntity;
        }
        else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signout(final String accessToken)throws SignOutRestrictedException{
        UserAuthTokenEntity authTokenEntity = userDao.checkLogin(accessToken);
        if(authTokenEntity == null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }
        authTokenEntity.setAccess_Token("");
        authTokenEntity.setLogout_At(ZonedDateTime.now());
        authTokenEntity = userDao.createAuthToken(authTokenEntity);
        return authTokenEntity;
    }
}
