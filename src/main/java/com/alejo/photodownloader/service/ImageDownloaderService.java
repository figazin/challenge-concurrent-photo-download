package com.alejo.photodownloader.service;

import com.alejo.photodownloader.entity.HouseListing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
@Slf4j
public class ImageDownloaderService {
    @Value("${image-save-path}")
    private String imagePath;
    public void saveImage(HouseListing house) {
        log.info("Saving house: {}", house);
        try {
            //TODO: adapt to other image extensions
            StringBuilder imageFilePath = new StringBuilder(imagePath);
            imageFilePath.append(house.getId())
                            .append("-")
                            .append(house.getAddress())
                            .append(".jpg");
            FileUtils.copyURLToFile(
                    new URL(house.getPhotoURL()),
                    new File(imageFilePath.toString()));
        } catch (Exception ex) {
            log.error("Error trying to save image {}", house.getPhotoURL(), ex);
        }
    }
}
