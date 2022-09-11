package com.mycustomblog.blog;

import com.mycustomblog.blog.controller.ProfileController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProfileControllerUnitTest {
    @Test
    public void real_profile이_조회된다(){
        //given
        String expectedProfiele = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfiele);
        env.addActiveProfile("oauth");
        env.addActiveProfile("db");

        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();
        //then
        assertThat(profile, equalTo(expectedProfiele));
    }

    @Test
    public void real_profile이_없으면_첫_번째가_조회된다(){
        //given
        String expectedProfiele = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfiele);
        env.addActiveProfile("db");

        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();
        //then
        assertThat(profile, equalTo(expectedProfiele));
    }

    @Test
    public void active_profile이_없으면_default가_조회된다(){
        //given
        String expectedProfiele = "default";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfiele);

        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();
        //then
        assertThat(profile, equalTo(expectedProfiele));
    }
}
