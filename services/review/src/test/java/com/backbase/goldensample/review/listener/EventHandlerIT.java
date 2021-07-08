package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.goldensample.review.Application;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.goldensample.review.v2.api.ReviewServiceApiController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class EventHandlerIT {

    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void createProductEventConsumingTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/service-api/v1/products"));

        // send message saying new product created
        // await receival of message
        // check if there is a review for that product


    }




}
