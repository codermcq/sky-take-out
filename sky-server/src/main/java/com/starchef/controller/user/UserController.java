package com.starchef.controller.user;

import com.starchef.constant.JwtClaimsConstant;
import com.starchef.context.BaseContext;
import com.starchef.dto.UserLoginDTO;
import com.starchef.entity.User;
import com.starchef.properties.JwtProperties;
import com.starchef.result.Result;
import com.starchef.service.UserService;
import com.starchef.utils.JwtUtil;
import com.starchef.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/user")
@Api(tags = "C端用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result wxLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户端微信登录: {}", userLoginDTO.getCode());
        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder().id(user.getId()).openid(user.getOpenid()).token(token).build();

        return Result.success(userLoginVO);
    }

    @GetMapping("/profile")
    @ApiOperation("查询用户信息")
    public Result<User> getProfile() {
        User user = userService.getProfile();
        return Result.success(user);
    }

    @PutMapping("/profile")
    @ApiOperation("更新用户信息")
    public Result updateProfile(@RequestBody User user) {
        log.info("更新用户信息: {}", user);
        user.setId(BaseContext.getCurrentId());
        userService.updateProfile(user);
        return Result.success();
    }

}
