package com.example.springaimultimodal;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
public class AskController {

    private final ChatClient chatClient;

    //  @Value("classpath:/forecast.jpg")
    @Value("classpath:/hi.jpeg")
    Resource forecastImageResource;

    public AskController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping(value = "/capture", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] live() throws IOException {
        Mat grabbedImage;

        try (FrameGrabber grabber = new OpenCVFrameGrabber(0)) {
            grabber.start();
            try (OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat()) {
                grabbedImage = converter.convert(grabber.grab());
            }
            grabber.stop();
        }

        BufferedImage image = Java2DFrameUtils.toBufferedImage(grabbedImage);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "jpg", os);
        return os.toByteArray();
    }

    @GetMapping("/ask")
    public Answer ask() throws IOException {
        byte[] live = live();

        return chatClient.prompt()
                .user(userSpec -> userSpec
                        .text("is this person good?")
                        .media(new Media(MimeTypeUtils.IMAGE_JPEG, live)))
                .call()
                .entity(Answer.class);
    }

    @GetMapping("/")
    String greet() {
        return "Hello World";
    }


}
