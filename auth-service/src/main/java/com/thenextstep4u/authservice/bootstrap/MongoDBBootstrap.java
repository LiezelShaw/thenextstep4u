package com.thenextstep4u.authservice.bootstrap;

import com.thenextstep4u.authservice.domain.AuthClientDetails;
import com.thenextstep4u.authservice.domain.User;
import com.thenextstep4u.authservice.enums.Authorities;
import com.thenextstep4u.authservice.repository.AuthClientRepository;
import com.thenextstep4u.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Profile({"dev","prod","test"})
@Component
public class MongoDBBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private AuthClientRepository authClientRepository;
    private UserRepository userRepository;

    public MongoDBBootstrap(AuthClientRepository authClientRepository, UserRepository userRepository) {
        this.authClientRepository = authClientRepository;
        this.userRepository = userRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {

        if (!authClientRepository.findByClientId("browser").isPresent()) {
            System.out.println("Saving AuthClientDetails for random-user");
            AuthClientDetails browserClientDetails = new AuthClientDetails();
            browserClientDetails.setClientId("browser");
            browserClientDetails.setClientSecret("$2a$10$fWNTd3H.u7G/aNROVQSifebOkZ2xzU5nUPOCI2Ld42M8E25/ljJqK");
            browserClientDetails.setScopes("ui");
            browserClientDetails.setGrantTypes("refresh_token,password,client_credentials");
            authClientRepository.save(browserClientDetails);
        }

        if (!userRepository.findByUsername("tns4u-system-user").isPresent()) {
            Set<Authorities> authorities = new HashSet<>();
            authorities.add(Authorities.ROLE_USER);

            System.out.println("Saving user for tns4u-system-user");
            User user = new User();
            user.setActivated(true);
            user.setAuthorities(authorities);
            user.setPassword("$2a$10$fWNTd3H.u7G/aNROVQSifebOkZ2xzU5nUPOCI2Ld42M8E25/ljJqK");
            user.setUsername("tns4u-system-user");
            userRepository.save(user);
        }

        if(!authClientRepository.findByClientId("blogging-service").isPresent()) {
            System.out.println("Saving AuthClientDetails for blogging-service");
            AuthClientDetails bloggingServiceClientDetails = new AuthClientDetails();
            bloggingServiceClientDetails.setClientId("blogging-service");
            bloggingServiceClientDetails.setClientSecret("$2a$10$fWNTd3H.u7G/aNROVQSifebOkZ2xzU5nUPOCI2Ld42M8E25/ljJqK");
            bloggingServiceClientDetails.setScopes("server");
            bloggingServiceClientDetails.setGrantTypes("refresh_token,client_credentials");
            authClientRepository.save(bloggingServiceClientDetails);
        }

    }
}
