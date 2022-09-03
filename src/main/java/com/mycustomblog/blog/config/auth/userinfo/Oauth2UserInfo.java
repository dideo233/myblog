package com.mycustomblog.blog.config.auth.userinfo;

import java.util.Map;

public interface Oauth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getUsername();
    String getPicture();
    Map<String, Object> getAttributes();
}
