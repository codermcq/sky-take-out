package com.starchef.service;

import com.starchef.dto.UserLoginDTO;
import com.starchef.entity.User;
import com.starchef.vo.UserLoginVO;

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
