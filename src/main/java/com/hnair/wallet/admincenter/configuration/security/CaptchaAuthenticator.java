package com.hnair.wallet.admincenter.configuration.security;

import com.hnair.wallet.admincenter.web.SystemController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/26/2018 2:18 PM
 */
public class CaptchaAuthenticator extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        HttpSession session = request.getSession();
        do {
            if (session != null) {
                // 判断用户是否需要输入验证码
                Object needCaptcha = session.getAttribute("need_captcha");
                if (needCaptcha == null || needCaptcha.toString().equals("1")) {
                    logger.info("此次登录无须用户验证码");
                    break;
                }
                // 需要输入验证码
                logger.info("开始校验用户验证码");
                String formCaptcha = request.getParameter("captcha");
                if (formCaptcha == null || formCaptcha.trim().equals("")) {
                    throw new BadCredentialsException("验证码不能为空");
                }
                SystemController.ImageCode captcha = (SystemController.ImageCode) session.getAttribute("session_captcha");
                if (captcha == null) {
                    throw new BadCredentialsException("系统繁忙，请刷新页面重试");
                }
                boolean expired = captcha.isExpired();
                if (expired) {
                    throw new BadCredentialsException("验证码已过期，请刷新页面重试");
                }
                String sessionCaptcha = captcha.getCode();
                if (!formCaptcha.trim().toLowerCase().equals(sessionCaptcha.toLowerCase())) {
                    throw new BadCredentialsException("验证码不正确");
                }
            }
        } while (false);
        Authentication authentication;
        try {
            authentication = super.attemptAuthentication(request, response);
            if (session != null) {
                session.setAttribute("need_captcha", "1");
            }
        } catch (AuthenticationException e) {
            if (session != null) {
                session.setAttribute("need_captcha", "0");
            }
            throw e;
        }
        return authentication;
    }


}
