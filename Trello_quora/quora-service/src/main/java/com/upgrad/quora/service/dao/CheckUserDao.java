package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CheckUserDao {
    @PersistenceContext
    private EntityManager entityManager;
    public UserEntity checkUserName(String username){
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("username", username).getSingleResult();
        }
        catch (Exception e){
            return  null;
        }
    }
    public UserEntity checkEmail(String email){
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        }
        catch (Exception e){
            return  null;
        }
    }
    public UserEntity signup(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }
    public UserAuthTokenEntity checkLogin(final String accessToken){
        try{
            return entityManager.createNamedQuery("userByAccessToken",UserAuthTokenEntity.class).setParameter("accessToken",accessToken).getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }
    public UserEntity getUserByUuid(String uuid){
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch (Exception e){
            return  null;
        }
    }

    public void deleteUser(UserEntity userEntity){
        entityManager.remove(userEntity);
    }
    public UserAuthTokenEntity getUser(final int id){
        try{
            return entityManager.createNamedQuery("userById",UserAuthTokenEntity.class).setParameter("id",id).getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }
}
