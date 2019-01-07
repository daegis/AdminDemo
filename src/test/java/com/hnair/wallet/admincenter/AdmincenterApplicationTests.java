package com.hnair.wallet.admincenter;

import cn.aegisa.selext.dao.service.ICommonService;
import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.model.AdmincenterRole;
import com.hnair.wallet.admincenter.noused.OrderConsumeInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdmincenterApplicationTests {

    @Autowired
    private ICommonService commonService;


    @Test
    public void contextLoads() {
        final List<AdmincenterRole> list = commonService.getList(AdmincenterRole.class);
        for (AdmincenterRole merchant : list) {
            System.out.println(JSON.toJSONString(merchant));
        }
    }

    @Test
    public void contextLoads1() {
        final List<OrderConsumeInfo> list = commonService.getList(OrderConsumeInfo.class);
        for (OrderConsumeInfo merchant : list) {
            System.out.println(JSON.toJSONString(merchant));
        }
    }

    @Test
    public void test01() {
        final List<OrderConsumeInfo> list = commonService.getList(OrderConsumeInfo.class);
        log.info("list size:{}", list.size());
        for (OrderConsumeInfo info : list) {
            log.info(JSON.toJSONString(info));
        }
    }

    @Test
    public void test09(){
        System.out.println(getCipherText("123456723932635074572B4843503079365039463257575745586E776D39392B4732484F7A51615449313079694D3D"));
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

    @Test
    public void test02(){
        System.out.println(getCipherText("399641".concat("487751324C463835376C4F3051744370755A466F724761587865313132324F67714A616A52466D457276633D")).equals("2F3053626D676F43496A394D6242446C4B6938315843596243457A70765252363271634D435A59785042343D"));
    }

}
