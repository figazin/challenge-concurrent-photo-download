package com.alejo.photodownloader.service;

import com.alejo.photodownloader.PhotoDownloaderApplication;
import com.alejo.photodownloader.PhotoDownloaderApplicationTests;
import com.alejo.photodownloader.shutdown.ShutdownService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ThreadPoolExecutor;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {PhotoDownloaderApplicationTests.class, PhotoDownloaderApplication.class})
@ActiveProfiles("test")
public class HousePhotoDownloaderServiceTest {

    @MockBean
    private HouseApiService houseApiService;
    @Autowired
    private ThreadPoolExecutor executor;
    @MockBean
    private ShutdownService shutdownService;
    @Autowired
    private HousePhotoDownloaderService housePhotoDownloaderService;

    @Test
    void whenDownloadPhotos_ShouldMakeTenRequestsAndShutdown() {
        housePhotoDownloaderService.downloadHousePhotos();
        verify(houseApiService, times(10)).requestHouseListingPage(anyInt());
        verify(shutdownService).finalizeApp();
    }
}
