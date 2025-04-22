package com.example.streaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {

    private static final String VIDEO_DIR = "videos/";
    private static final String FFMPEG_PATH = "/usr/local/bin/ffmpeg"; // Change this path based on your system

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path videoPath = Paths.get(VIDEO_DIR + filename);

        Files.createDirectories(videoPath.getParent());
        file.transferTo(videoPath);

        convertToHLS(videoPath.toFile());

        return ResponseEntity.ok("Uploaded and converted: " + filename);
    }

    private void convertToHLS(File videoFile) throws IOException, InterruptedException {
        String outputDir = VIDEO_DIR + UUID.randomUUID().toString();
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        String command = String.format("%s -i %s -profile:v baseline -level 3.0 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls %s/playlist.m3u8",
                FFMPEG_PATH, videoFile.getAbsolutePath(), outputDir);

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> streamVideo(@PathVariable String filename) throws IOException {
        File videoFile = new File(VIDEO_DIR + filename);
        if (videoFile.exists()) {
            byte[] content = Files.readAllBytes(videoFile.toPath());
            return ResponseEntity.ok(content);
        }
        return ResponseEntity.notFound().build();
    }
}
