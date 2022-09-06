package com.mycustomblog.blog.service;

import com.mycustomblog.blog.config.auth.PrincipalImpl;
import com.mycustomblog.blog.config.auth.UserInfoFactory;
import com.mycustomblog.blog.config.auth.userinfo.Oauth2UserInfo;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.domain.Role;
import com.mycustomblog.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PrincipalServiceImpl extends DefaultOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserInfoFactory userInfoFactory;

    @Value("${adminInfo.username}")
    private String adminUsername;
    @Value("${adminInfo.picUrl}")
    private String adminPicUrl;
    @Value("${adminInfo.email}")
    private String adminEmail;
    @Value("${adminInfo.providerId}")
    private String adminProviderId;
    @Value("${adminInfo.provider}")
    private String adminProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Oauth2UserInfo userInfo = userInfoFactory.makeOauth2UserInfo(userRequest, super.loadUser(userRequest)); //OAuth2 UserInfo 반환
        Member member = getOrJoinMember(userInfo); //UserInfo 정보 DB 삽입

        return new PrincipalImpl(member, userInfo.getAttributes()); //principal 객체 리턴 -> SecurityContextHoler로 보관
    }

    private Member getOrJoinMember(Oauth2UserInfo userInfo) {
        Member member = memberRepository.findByUserId(userInfo.getProviderId());

        if(member == null) { //DB에 User 정보 없을 시
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

    //PostConstruct : 의존성 주입 후 수행하는 메서드. 서버 구동 시 관리자 등록 기능 수행
    @PostConstruct
    public void insertAdmin(){
        Member admin = memberRepository.findByEmail(adminEmail);

        if(admin == null){
            admin = Member.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .picUrl(adminPicUrl)
                    .userId(adminProviderId)
                    .providerId(adminProviderId)
                    .provider(adminProvider)
                    .role(Role.ADMIN)
                    .build();

            memberRepository.save(admin);
        }
    }
}
