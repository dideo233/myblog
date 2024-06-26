package com.mycustomblog.blog.config.auth;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.mycustomblog.blog.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalImpl implements OAuth2User, Serializable {
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
        System.out.println(member.getRole().getValue());
        collect.add(new SimpleGrantedAuthority(member.getRole().getValue()));
        return collect;
    }

    @Override
    public String getName() {
        return member.getUsername();
    }
    public Long getUsernum(){ return member.getUsernum(); }
    public String getMemberPicUrl(){
        return member.getPicUrl();
    }
    public Member getMember(){
        return member;
    }
}