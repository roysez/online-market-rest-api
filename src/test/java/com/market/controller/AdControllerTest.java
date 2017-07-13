package com.market.controller;

import com.market.Application;
import com.market.CustomRepositoryRestConfigurer;
import com.market.repository.AdRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by roysez on 13.07.2017.
 * 15:39
 * Package : com.market.domain.core.ad
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {Application.class, CustomRepositoryRestConfigurer.class})
public class AdControllerTest {

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    AdController adController;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void publish() throws Exception {
    }

    @Test
    public void expire() throws Exception {

    }

}