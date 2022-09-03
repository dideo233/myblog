package com.mycustomblog.blog.config.auth.userinfo;

import java.util.Map;

public class UserInfoKakao implements Oauth2UserInfo{
    private Map<String, Object> attributes;

    public UserInfoKakao(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }
    @Override
    public String getProvider() {
        return "kakao";
    }
    @Override
    public String getEmail() {
        Map<String, Object> kakao_account = (Map) attributes.get("kakao_account");
        return kakao_account.get("email").toString();
    }
    @Override
    public String getUsername() {
        Map<String, Object> kakao_account = (Map) attributes.get("kakao_account");
        Map<String, Object> profile = (Map) kakao_account.get("profile");
        return profile.get("nickname").toString();
    }

    //해당 값 null
    @Override
    public String getPicture() {
//        Map<String,Object> kakao_account = (Map)attributes.get("kakao_account");
//        Map<String,Object> profile = (Map) kakao_account.get("profile");
//        return profile.get("profile_image_url").toString();
        return "default_profile_image_url";
    }
}