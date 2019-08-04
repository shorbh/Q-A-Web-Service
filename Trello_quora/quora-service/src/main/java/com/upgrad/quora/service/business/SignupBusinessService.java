package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.CheckUserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {
    @Autowired
    private CheckUserDao checkUser;
    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;
    public boolean checkUserName(String username) throws SignUpRestrictedException{
        if( (checkUser.checkUserName(username) != null)){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        else {
            return true;
        }
    }
    public boolean checkEmail(String email) throws SignUpRestrictedException{
        if( (checkUser.checkEmail(email) != null)){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }
        else {
            return true;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity){
        String[] encrypt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encrypt[0]);
        userEntity.setPassword(encrypt[1]);
        return checkUser.signup(userEntity);
    }
}
