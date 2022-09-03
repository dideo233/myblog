package com.mycustomblog.blog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseTime{
 	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
	    private Long usernum;
	
	    @Column(nullable = false)
	    private String username;
	
	    @Column(nullable = false)
	    private String userId;
 	
	    @Column(nullable = false, unique = true)
	    private String email;
	
	    private String picUrl;
	
	    @Enumerated(EnumType.STRING)
	    private Role role;
	    	
	    //Oauth2 프로바이더
	    private String providerId;
	    private String provider;
	    
		@Builder 
		public Member(String username, String email, String picUrl, Role role,String userId, String provider, String providerId) {
		    this.username = username;
		    this.email = email;
		    this.picUrl = picUrl;
		    this.userId = userId;
		    this.role = role;
		    this.provider = provider;
		    this.providerId = providerId;
		}
	    
	    public void changeUsername(String username) {
	        this.username = username;
	    }
}
