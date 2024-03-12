package com.project.pi.service.impl;

import com.project.module.common.entity.TbUser;
import com.project.module.common.model.SocialUserInfo;
import com.project.module.common.model.TokenInfo;
import com.project.module.common.service.AuthService;
import com.project.pi.constant.PiSocialConstant;
import com.project.pi.sdk.PiSDKService;
import com.project.pi.sdk.model.PiUserDTO;
import com.project.pi.service.AuthPiService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

@Service
public class AuthPiServiceImpl implements AuthPiService {

    @Resource
    private AuthService authService;

    @Resource
    private PiSDKService piSDKService;

    @Override
    public TokenInfo login(Map<String, Object> parameters) {
        Assert.isTrue(Objects.nonNull(parameters), "参数不能为空");

        // 访问令牌
        String accessToken = parameters.getOrDefault("accessToken", "").toString();

        // 邀请码
        String inviteCode = parameters.getOrDefault("inviteCode", "").toString();

        // 校验
        Assert.isTrue(StringUtils.isNotBlank(accessToken), "访问令牌不能为空");

        // 验证通过后的逻辑处理...

        PiUserDTO piUserDTO = piSDKService.login(accessToken);

        // 用户名
        String username = piUserDTO.getUsername();
        String uid = piUserDTO.getUid();
        String avatar = piUserDTO.getAvatar();

        // 构造第三方用户数据
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        socialUserInfo.setNickname(username);
        socialUserInfo.setAvatar(avatar);
        socialUserInfo.setGender(null);
        socialUserInfo.setPlatform(PiSocialConstant.PLATFORM_PI);
        socialUserInfo.setClientId(PiSocialConstant.CLIENT_ID_PI);
        socialUserInfo.setOpenId(uid);
        socialUserInfo.setUnionId(username);
        socialUserInfo.setInviteCode(inviteCode);

        // 使用第三方用户数据进行登录
        TbUser user = authService.buildSocialUser(socialUserInfo);
        // 构造令牌信息
        TokenInfo tokenInfo = authService.buildTokenInfo(user);
        // 返回令牌信息
        return tokenInfo;
    }

    @Override
    public TokenInfo register(Map<String, Object> parameters) {
        return authService.register(parameters);
    }

    @Override
    public Boolean logout(Map<String, Object> parameters) {
        return authService.logout(parameters);
    }

}
