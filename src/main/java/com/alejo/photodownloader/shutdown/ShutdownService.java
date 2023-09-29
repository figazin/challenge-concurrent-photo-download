package com.alejo.photodownloader.shutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ShutdownService {

    @Autowired
    private ApplicationContext appContext;

    public void finalizeApp() {
        SpringApplication.exit(appContext, () -> 0);
    }
}
