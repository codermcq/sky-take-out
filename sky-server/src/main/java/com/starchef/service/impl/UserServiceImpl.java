package com.starchef.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starchef.constant.MessageConstant;
import com.starchef.context.BaseContext;
import com.starchef.dto.UserLoginDTO;
import com.starchef.entity.User;
import com.starchef.exception.LoginFailedException;
import com.starchef.mapper.UserMapper;
import com.starchef.properties.WeChatProperties;
import com.starchef.service.UserService;
import com.starchef.utils.HttpClientUtil;
import com.starchef.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("appid", weChatProperties.getAppid());
        loginMap.put("secret", weChatProperties.getSecret());
        loginMap.put("js_code", code);
        loginMap.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, loginMap);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = (String) jsonObject.get("openid");

        // 检查openid是否为空
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 检查数据库是否存在openid
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 修改用户信息
     * @param user
     */
    @Override
    public void updateProfile(User user) {
        userMapper.update(user);
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public User getProfile() {
        Long userId = BaseContext.getCurrentId();
        return userMapper.getById(userId);
    }
}
