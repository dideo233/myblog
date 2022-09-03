package com.mycustomblog.blog.config.auth;

import com.mycustomblog.blog.config.auth.userinfo.Oauth2UserInfo;
import com.mycustomblog.blog.config.auth.userinfo.UserInfoGoogle;
import com.mycustomblog.blog.config.auth.userinfo.UserInfoKakao;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class UserInfoFactory {
    //소셜 로그인 종류에 따라 oAuth2User 리턴
    public Oauth2UserInfo makeOauth2UserInfo(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("google")) {
            return new UserInfoGoogle(oAuth2User.getAttributes());
        }else if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            return new UserInfoKakao(oAuth2User.getAttributes());
        } else {
            return null;
        }
    }
}
