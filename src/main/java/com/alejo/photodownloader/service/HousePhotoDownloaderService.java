package com.alejo.photodownloader.service;

import com.alejo.photodownloader.shutdown.ShutdownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
@Slf4j
public class HousePhotoDownloaderService {

    @Value("${image-save-path}")
    private String imagePath;
    private final ThreadPoolExecutor executor;
    private final HouseApiService houseApiService;
    private final ShutdownService shutdownService;

    HousePhotoDownloaderService(ThreadPoolExecutor executor,
                                HouseApiService houseApiService,
                                ShutdownService shutdownService) {
        this.executor = executor;
        this.houseApiService = houseApiService;
        this.shutdownService = shutdownService;
    }

    //Run once and never again
    @Scheduled(initialDelay = 100, fixedDelay=Long.MAX_VALUE, timeUnit = TimeUnit.NANOSECONDS )
    public void downloadHousePhotos() {
        log.info("Starting photo download process. Images will be stored in: {}", imagePath);
        //TODO change execute for submit so we can check if every Future has finished
        IntStream.rangeClosed(1,10)
                .forEach(i -> executor.execute(() -> houseApiService.requestHouseListingPage(i)));
        while(executor.getActiveCount() != 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shutdownService.finalizeApp();
    }
}