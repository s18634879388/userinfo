package com.ninehcom.mapper;


import com.ninehcom.entity.UserInfo;


/**
 * UserUserinfo的Mapper，用于Mybatis
 *
 * @author shenjizhe
 * @version 1.0.0
 */
public interface UserInfoMapper {

    UserInfo selectUserInfoById(String Id);
    int insertUser(String Id, String nickName);


}
