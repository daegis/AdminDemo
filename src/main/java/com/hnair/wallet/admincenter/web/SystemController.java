package com.hnair.wallet.admincenter.web;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Using IntelliJ IDEA.
 * 正常统一跳转controller
 *
 * @author XIANYINGDA at 7/20/2018 5:09 PM
 */
@Controller
@Slf4j
public class SystemController {

    // 主布局控制器
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    // 登陆控制器 **重要 不要动
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // 登出控制器 **重要 不要动
    @RequestMapping("/logout")
    public String logout() {
        return "index";
    }

    // 后台首页控制器
    @RequestMapping("/bgMain")
    public String toBackgroundMain() {
        return "welcome";
    }

    @GetMapping("/captcha/get")
    public void createCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode(request);
        HttpSession session = request.getSession();
        log.info("session value:{}", imageCode.code);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
        imageCode.free();
        session.setAttribute("session_captcha", imageCode);
    }

    private ImageCode createImageCode(HttpServletRequest request) {
        int width = 150; //验证码图片长度
        int height = 50;//宽

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font(null, Font.BOLD, 36));
        StringBuilder sRand = new StringBuilder();
        String seed = "A23BCDE234FGHJ567KL89MNPQRS89TUVWXYZ";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(seed.charAt(random.nextInt(seed.length())));
            sRand.append(rand);
            g.setColor(new Color(203, 3, 3));
            g.drawString(rand, 32 * i + 12, 40);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), 180);
    }


    @Data
    public class ImageCode {
        private BufferedImage image;
        private String code;
        private LocalDateTime expireTime;

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }

        ImageCode(BufferedImage image, String code, int expireIn) {
            this.image = image;
            this.code = code;
            this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
        }

        void free() {
            this.image = null;
        }
    }

}
