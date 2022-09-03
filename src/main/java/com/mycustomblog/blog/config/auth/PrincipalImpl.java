package com.mycustomblog.blog.config.auth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.mycustomblog.blog.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalImpl implements OAuth2User{
    private Member member;
    private Map<String, Object> attributes;

    //Oauth2User
    public PrincipalImpl(Member member, Map<String, Object> attributes) {
        this.member =  member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getName() {
        return member.getUsername();
    }

}