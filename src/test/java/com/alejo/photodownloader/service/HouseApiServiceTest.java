package com.alejo.photodownloader.service;

import com.alejo.photodownloader.PhotoDownloaderApplication;
import com.alejo.photodownloader.PhotoDownloaderApplicationTests;
import com.alejo.photodownloader.entity.HouseListing;
import com.alejo.photodownloader.entity.HouseListingResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {PhotoDownloaderApplicationTests.class, PhotoDownloaderApplication.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HouseApiServiceTest {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private ThreadPoolExecutor executor;
    @MockBean
    private ImageDownloaderService imageDownloaderService;
    @Autowired
    private HouseApiService houseApiService;

    @Test
    @Order(1)
    void givenTwoHouses_whenRequestAPI_shouldCallSaveImageTwice() {
        HouseListingResponse houseListingResponse = new HouseListingResponse();
        houseListingResponse.setHouses(Arrays.asList(new HouseListing(), new HouseListing()));
        when(restTemplate.getForObject(anyString(), any())).thenReturn(houseListingResponse);
        houseApiService.requestHouseListingPage(1);
        verify(imageDownloaderService, times(2)).saveImage(any());
    }

    @Test
    @Order(2)
    void givenException_whenRequestAPI_shouldRetry() {
        HouseListingResponse houseListingResponse = new HouseListingResponse();
        houseListingResponse.setHouses(Arrays.asList(new HouseListing(), new HouseListing()));
        when(restTemplate.getForObject(anyString(), any()))
                .thenThrow(HttpServerErrorException.class);
        try {
            houseApiService.requestHouseListingPage(1);
        } catch (Exception e){}
        verify(restTemplate, times(5)).getForObject(anyString(), any());
    }
}
