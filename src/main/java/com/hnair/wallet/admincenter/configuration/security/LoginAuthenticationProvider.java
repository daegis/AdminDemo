package com.hnair.wallet.admincenter.configuration.security;

import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/22/2018 9:13 PM
 */
@Component
@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        log.info("username:{},password:{}", username, password);
        final AdmincenterOperator user = (AdmincenterOperator) userDetailService.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("用户不存在");
        }
        if (!checkUserPassword(user, password)) {
            throw new BadCredentialsException("密码错误");
        }
        Collection<? extends GrantedAuthority> collection = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, collection);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    private static boolean checkUserPassword(AdmincenterOperator user, String formPassword) {
        final String operatorUniqueSalt = user.getOperatorUniqueSalt();
        final String plainPassword = formPassword.concat(operatorUniqueSalt);
        final String cipherText = getCipherText(plainPassword);
        final String password = user.getPassword();
        return Objects.equals(cipherText, password);
    }

    private static String getCipherText(String plainText) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            sha256Digest.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] digestByteArray = sha256Digest.digest();
            final byte[] base64ByteArray = Base64.getEncoder().encode(digestByteArray);
            return DatatypeConverter.printHexBinary(base64ByteArray);
        } catch (Exception ignored) {
        }
        return null;
    }
}
