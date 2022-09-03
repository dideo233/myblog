package com.mycustomblog.blog.service;

import com.mycustomblog.blog.config.auth.PrincipalImpl;
import com.mycustomblog.blog.config.auth.UserInfoFactory;
import com.mycustomblog.blog.config.auth.userinfo.Oauth2UserInfo;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.domain.Role;
import com.mycustomblog.blog.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalServiceImpl extends DefaultOAuth2UserService{
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserInfoFactory userInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Oauth2UserInfo userInfo = userInfoFactory.makeOauth2UserInfo(userRequest, super.loadUser(userRequest)); //OAuth2 UserInfo 반환
        Member member = getOrJoinMember(userInfo); //UserInfo 정보 DB 삽입

        //
//		System.out.println("load User");
//		System.out.println(userRequest.getAccessToken());
//		System.out.println(userRequest.getClientRegistration().getRegistrationId());
//		System.out.println(userRequest.getAdditionalParameters());
//
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        String provider = userRequest.getClientRegistration().getRegistrationId();    //google
//        String providerId = oAuth2User.getAttribute("sub");
//
//        System.out.println(providerId);
//        System.out.println(oAuth2User);
//        String username = provider+"_"+providerId;
//
//        String email = oAuth2User.getAttribute("email");
//        Role role = Role.USER;
//        System.out.println(email);
//        System.out.println(role);
//        Member member = memberRepository.findByUserId(providerId);
        //
        return new PrincipalImpl(member, userInfo.getAttributes()); //principal 객체 리턴 -> SecurityContextHoler로 보관
    }

    private Member getOrJoinMember(Oauth2UserInfo userInfo) {
        Member member = memberRepository.findByUserId(userInfo.getProviderId());

        if(member == null) {
            //중복 이메일 체크
            if(memberRepository.findByEmail(userInfo.getEmail()) != null)
                throw new OAuth2AuthenticationException("duplicateEmail");

            member = Member.builder()
                    .username(userInfo.getUsername())
                    .picUrl(userInfo.getPicture())
                    .email(userInfo.getEmail())
                    .userId(userInfo.getProviderId())
                    .providerId(userInfo.getProviderId())
                    .provider(userInfo.getProvider())
                    .role(Role.USER)
                    .build();

            memberRepository.save(member);
        }

        if(!member.getUsername().equals(userInfo.getUsername())){
            member.changeUsername(userInfo.getUsername());
        }

        return member;
    }

}
