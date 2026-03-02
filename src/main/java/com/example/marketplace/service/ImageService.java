package com.example.marketplace.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Scans /static/images/ on the classpath at startup and stores the filenames
// Works both when running from IDE and from a packaged JAR
@Service
public class ImageService {

    private final List<String> imageFilenames = new ArrayList<>();

    @PostConstruct
    public void scanImages() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/static/images/*");

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename != null) {
                imageFilenames.add(filename);
            }
        }

        imageFilenames.sort(String::compareToIgnoreCase);
        System.out.println("ImageService: Found " + imageFilenames.size() + " images in /static/images/");
    }

    // Returns all image filenames (e.g. "Rose.png", "Tulip.png")
    public List<String> getAvailableImages() {
        return imageFilenames;
    }
}

