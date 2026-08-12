package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 更新用户信息
     * @param user
     */
    void updateProfile(User user);

    /**
     * 查询当前用户信息
     * @return
     */
    User getProfile();
}
