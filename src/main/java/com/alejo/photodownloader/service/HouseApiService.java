package com.alejo.photodownloader.service;

import com.alejo.photodownloader.entity.HouseListing;
import com.alejo.photodownloader.entity.HouseListingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class HouseApiService {

    private final RestTemplate restTemplate;
    private final ThreadPoolExecutor executor;
    private final ImageDownloaderService imageDownloaderService;

    HouseApiService(RestTemplate restTemplate,
                    ThreadPoolExecutor executor,
                    ImageDownloaderService imageDownloaderService) {
        this.restTemplate = restTemplate;
        this.executor = executor;
        this.imageDownloaderService = imageDownloaderService;
    }

    String apiUrl = "http://app-homevision-staging.herokuapp.com/api_project/houses";

    //TODO: define retry strategy
    @Retryable(maxAttempts=5, value = HttpServerErrorException.class,
            backoff = @Backoff(delay = 150, multiplier = 2))
    public void requestHouseListingPage(int page) {
        log.info("Calling API with url: {} and page: {}", apiUrl, page);
        HouseListingResponse houseListingResponse =
                restTemplate.getForObject(apiUrl + "?page=" + page,
                        HouseListingResponse.class);
        log.info("Page {} requested successfully.", page);
        for (HouseListing house : houseListingResponse.getHouses()) {
            executor.execute(() -> imageDownloaderService.saveImage(house));
        }
    }

    //TODO: should this delete all of the downloaded photos and stop execution?
    @Recover
    public void returnLastValue() {
        log.error("We weren't able to download some of the photos");
    }
}
