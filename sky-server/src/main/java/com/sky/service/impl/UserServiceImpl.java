package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
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

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务, 获取当前微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断openid是否为空, 如果为空抛出异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断当前是否为新用户
        User user = userMapper.getByOpenid(openid);

        // 如果是新用户, 自动完成注册
        if (user == null) {
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }
        
        return user;
    }

    private String getOpenid(String code) {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("appid", weChatProperties.getAppid());
        loginMap.put("secret", weChatProperties.getSecret());
        loginMap.put("js_code", code);
        loginMap.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, loginMap);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
