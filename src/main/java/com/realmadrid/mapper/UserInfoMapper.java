package com.realmadrid.mapper;


import com.realmadrid.entity.UserInfo;


/**
 *
 */
public interface UserInfoMapper {

    UserInfo selectUserInfoById(String Id);
    int insertUser(UserInfo userInfo);
    int updateUserPass(UserInfo userInfo);

    UserInfo selectUserInfoByPhone(String mobileNum);
}
