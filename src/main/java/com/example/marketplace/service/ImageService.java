package com.example.marketplace.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// scans /static/images once at startup so add/edit item can show a dropdown
// works in ide + jar, hence i don't have to hardcode filenames
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

    // hands back filenames like "Rose.png"
    public List<String> getAvailableImages() {
        return imageFilenames;
    }
}
