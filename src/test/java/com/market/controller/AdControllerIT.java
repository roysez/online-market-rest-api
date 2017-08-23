package com.market.controller;

import com.market.Application;
import com.market.CustomRepositoryRestConfigurer;
import com.market.entity.Ad;
import com.market.entity.User;
import com.market.repository.UserRepository;
import com.market.service.AdService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by roysez on 13.07.2017.
 * 15:52
 * Package : com.market.domain.core.ad
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes =  {Application.class, CustomRepositoryRestConfigurer.class})
public class AdControllerIT {

    @Autowired
    private AdService adService;

    @Autowired
    private UserRepository userRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();



    @Autowired
    private TokenStore tokenStore;

    @Before
    public void setUp(){
        User user = new User();
        user.setId(1L);
                user.setPhoneNumber("+380935158848");
                user.setPassword("user");
                user.setUsername("user");
                user.setRole(User.Role.ADMIN);

        Ad ad = new Ad().setUser(user)
            .setId(1L)
            .setCurrency(Ad.Currency.EUR)
            .setLocation(new Ad.Location("Lviv","Shevchenkivsk"))
            .setContent("test")
                .setRate(BigDecimal.TEN)
                .setType(Ad.Type.SELL)
                .setStatus(Ad.Status.NEW);

        userRepository.save(user);
        adService.save(ad);


        final OAuth2AccessToken token = new DefaultOAuth2AccessToken("FOO");
        final ClientDetails client = new BaseClientDetails("client", null, "read write", "client_credentials", "ROLE_CLIENT");
        final OAuth2Authentication authentication = new OAuth2Authentication(
                new TokenRequest(null, "client", null, "client_credentials").createOAuth2Request(client), null);

        tokenStore.storeAccessToken(token, authentication);
    }
    @Test
    public void findOneTest(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer FOO");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Resource<Ad>> responseEntity =
                restTemplate.exchange("http://localhost:8080/ads/1",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<Resource<Ad>>() {});

        Resource<Ad> adResource = responseEntity.getBody();

        System.out.println(adResource.getContent());
        assertThat(adResource.getContent().getContent(),is("test"));

//        ImmutableList<Long> listOfId = list.getContent().stream()
//                .map(Ad::getId)
//                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
//        listOfId.forEach(System.out::println);
//        assertThat(listOfId, Matchers.containsInAnyOrder(1,2));


    }

    @Test
    public void postOneTest(){
        RestTemplate restTemplate = new RestTemplate();

        Ad ad = new Ad().setUser(userRepository.findOne(1L))
                .setCurrency(Ad.Currency.EUR)
                .setLocation(new Ad.Location("Lviv","Shevchenkivsk"))
                .setContent("testPost")
                .setRate(BigDecimal.TEN)
                .setType(Ad.Type.SELL)
                .setStatus(Ad.Status.NEW);

        HttpEntity<Ad> request = new HttpEntity<>(ad);

        ResponseEntity<Resource<Ad>> response = restTemplate
                .exchange("http://localhost:8080/ads", HttpMethod.POST, request,
                        new ParameterizedTypeReference<Resource<Ad>>() {});

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        ad = response.getBody().getContent();

        assertThat(ad, notNullValue());
        assertThat(ad.getContent(), is("testPost"));
        System.out.println(ad);
    }

    @Test
    public void publishOneTest(){

        Ad ad = adService.findOne(1L);

        assert(ad.getStatus().equals(Ad.Status.NEW));

                restTemplate.exchange("http://localhost:8080/ads/1/publishing",
                        HttpMethod.PUT,
                        null,
                        new ParameterizedTypeReference<Resource<Ad>>() {});

        ad = adService.findOne(1L);

        assertThat(ad.getStatus(),is(Ad.Status.PUBLISHED));

    }

    @Test
    public void expireOneTest(){

        Ad ad = adService.findOne(1L);
        ad.setStatus(Ad.Status.PUBLISHED);
        adService.save(ad);
        assert(ad.getStatus().equals(Ad.Status.PUBLISHED));

        restTemplate.exchange("http://localhost:8080/ads/1/expiration",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Resource<Ad>>() {});

        ad = adService.findOne(1L);

        assertThat(ad.getStatus(),is(Ad.Status.EXPIRED));

    }
}