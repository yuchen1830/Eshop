package com.eshop.dto;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.List;

@Getter
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ImageCaptcha {
    private int width = 160;
    private int height = 40;
    private int digitsCount = 4;
    private int lineCount = 4;
    private String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    @Getter
    private String code;

    @Getter
    private BufferedImage buffImg;

    private final Random random = new Random();

    private static final List<Font> AVAILABLE_FONTS = List.of(
            new Font("Ravie", Font.PLAIN, 0),
            new Font("Antique Olive Compact", Font.PLAIN, 0),
            new Font("Fixedsys", Font.PLAIN, 0),
            new Font("Wide Latin", Font.PLAIN, 0),
            new Font("Gill Sans Ultra Bold", Font.PLAIN, 0)
    );

    @PostConstruct
    public void init(){
        createImage();
    }
    private void createImage() {
        int fontWidth = width/digitsCount;
        int fontHeight = height - 5;
        int codeY = height - 8;

        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = (Graphics2D) buffImg.getGraphics();
        // Enable anti-aliasing for better quality
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        graph.setColor(getRandomColor(200, 250));
        graph.fillRect(0,0,width, height);
        drawRandomLines(graph);
        addNoise();
        drawCaptchaText(graph, fontWidth, fontHeight, codeY);
        graph.dispose();
    }

    private void drawRandomLines(Graphics2D graph) {
        for(int i = 0; i < lineCount; i++){
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            graph.setColor(getRandomColor(1,255));
            graph.drawLine(xs, ys,xe,ye);
        }
    }

    private void addNoise() {
        float noiseRate = 0.01f;
        int noiseArea = (int) (noiseRate * width * height);
        for (int i = 0; i< noiseArea; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buffImg.setRGB(x, y, random.nextInt(255));
        }
    }

    private void drawCaptchaText(Graphics2D graph, int fontWidth, int fontHeight, int codeY){
        String captchaText = randomStr(digitsCount);
        this.code = captchaText;
        for(int i = 0; i < digitsCount; i++) {
            String character = captchaText.substring(i, i + 1);
            graph.setColor(getRandomColor(1, 255));
            graph.setFont(getRandomFont(fontHeight));
            double rotation = (random.nextDouble() - 0.5) * Math.PI / 4; // random rotation up to 45
            graph.rotate(rotation, i * fontWidth + fontWidth/2, codeY - fontHeight/2);
            graph.drawString(character, i * fontWidth + 3, codeY);
            graph.rotate(-rotation, i * fontWidth + fontWidth/2, codeY - fontHeight/2);
        }
    }

    private String randomStr(int length) {
        if(StringUtils.isEmpty(allowedChars)) {
            throw new IllegalArgumentException("Allowed Characters not Configured");
        }
        StringBuilder result = new StringBuilder(length);
        int len = allowedChars.length();
        for(int i = 0; i< length; i++) {
            result.append(allowedChars.charAt(random.nextInt(len)));
        }
        return result.toString();
    }

    private Color getRandomColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);
        return new Color(
                fc + random.nextInt(bc - fc),
                fc + random.nextInt(bc - fc),
                fc + random.nextInt(bc - fc)
        );
    }

    private Font getRandomFont(int size) {
        Font baseFont = AVAILABLE_FONTS.get(random.nextInt(AVAILABLE_FONTS.size()));
        return baseFont.deriveFont(Font.BOLD, size);
    }

    public void write(OutputStream sos) throws IOException {
        try {
            ImageIO.write(buffImg, "png", sos);
        } finally {
            sos.close();
        }
    }

    @Override
    public String toString() {
        return String.format("CAPTCHA[%dx%d, digits=%d]", width, height, digitsCount);
    }
}
