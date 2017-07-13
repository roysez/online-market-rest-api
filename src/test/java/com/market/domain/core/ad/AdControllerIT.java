package com.market.domain.core.ad;

import com.market.Application;
import com.market.CustomRepositoryRestConfigurer;
import com.market.entity.Ad;
import com.market.entity.User;
import com.market.repository.AdRepository;
import com.market.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
/**
 * Created by roysez on 13.07.2017.
 * 15:52
 * Package : com.market.domain.core.ad
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT,classes =  {Application.class, CustomRepositoryRestConfigurer.class})
public class AdControllerIT {

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp(){
        User user = new User().setId(2L).setPhoneNumber("+380935158848");

        Ad ad = new Ad().setUser(user)
            .setId(2L)
            .setCurrency(Ad.Currency.EUR)
            .setLocation(new Ad.Location("Lviv","Shevchenkivsk"))
            .setContent("test")
                .setRate(BigDecimal.TEN)
                .setType(Ad.Type.SELL)
                .setStatus(Ad.Status.NEW);

        userRepository.save(user);
        adRepository.save(ad);
    }
    @Test
    public void findOneTest(){
        ResponseEntity<Resource<Ad>> responseEntity =
                restTemplate.exchange("http://localhost:8080/ads/2",
                        HttpMethod.GET,
                        null,
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
    public void publishOneTest(){

        Ad ad = adRepository.findOne(2L);
        System.out.println(ad);
        assert(ad.getStatus().equals(Ad.Status.NEW));

                restTemplate.exchange("http://localhost:8080/ads/2/publishing",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Resource<Ad>>() {});

         ad = adRepository.findOne(2L);
        System.out.println(ad);

        assertThat(ad.getStatus(),is(Ad.Status.PUBLISHED));

    }
}