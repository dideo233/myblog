package com.mycustomblog.blog.config.auth.userinfo;

import java.util.Map;

public class UserInfoGoogle implements Oauth2UserInfo {
    private Map<String,Object> attributes;

                        //OAuth2User.getAttributes();
    public UserInfoGoogle(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");	}
    @Override
    public String getProvider() {
        return "google";
    }
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }
    @Override
    public String getPicture() {
        return (String) attributes.get("picture");
    }
}
