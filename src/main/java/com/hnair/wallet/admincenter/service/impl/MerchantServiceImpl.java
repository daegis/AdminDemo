package com.hnair.wallet.admincenter.service.impl;

import cn.aegisa.selext.dao.service.ICommonService;
import cn.aegisa.selext.dao.utils.PageFinder;
import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.constant.CipherTypeEnum;
import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.noused.MerchantTradeCipher;
import com.hnair.wallet.admincenter.service.IMerchantService;
import com.hnair.wallet.admincenter.utils.SecurityUtil;
import com.hnair.wallet.admincenter.vo.MerchanrUpdateQueryVo;
import com.hnair.wallet.admincenter.vo.MerchantDetailVo;
import com.hnair.wallet.admincenter.vo.pay.AlipayConfigurationParams;
import com.hnair.wallet.admincenter.vo.pay.WeixinPayConfigurationParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/21/2018 4:08 PM
 */
@Service
@Slf4j
public class MerchantServiceImpl implements IMerchantService {

    @Autowired
    private ICommonService commonService;

    @Override
    public PageFinder<Merchant> getMerchantListOnPage(int pageSize, int pageNumber) {
        PageFinder<Merchant> pageFinder = new PageFinder<>(pageNumber, pageSize);
        // 获取记录总条数
        Integer total = commonService.getBySqlId(Merchant.class, "pageCount");
        int start = pageSize * (pageNumber - 1);
        List<Merchant> merchantList = commonService.getListBySqlId(Merchant.class, "selectForPageData", "start", start, "pageSize", pageSize);
        pageFinder.setData(merchantList);
        pageFinder.setRowCount(total);
        return pageFinder;
    }

    @Override
    public MerchantDetailVo getMerchantDetail(Integer id) {
        MerchantDetailVo result = new MerchantDetailVo();
        // 查询出主要对象
        Merchant merchant = commonService.get(id, Merchant.class);
        result.setMerchant(merchant);
        // 构建alipay对象
        AlipayConfigurationParams ali = new AlipayConfigurationParams();
        List<MerchantTradeCipher> cipherList = commonService.getList(MerchantTradeCipher.class, "merchantId", merchant.getMerchantId());
        Map<Integer, List<MerchantTradeCipher>> cipherGroup = cipherList.stream().collect(Collectors.groupingBy(MerchantTradeCipher::getTradeType));
        // 设置支付宝参数
        // 支付宝 appid-200
        List<MerchantTradeCipher> appidList = cipherGroup.get(CipherTypeEnum.ALIPAY_APP_ID.getType());
        if (appidList != null && appidList.size() > 0) {
            MerchantTradeCipher next = appidList.listIterator().next();
            ali.setAppid(next.getTradeToken());
        }
        // 支付宝 privateKey-201
        List<MerchantTradeCipher> privateKeyList = cipherGroup.get(CipherTypeEnum.ALIPAY_PRIVATE_KEY.getType());
        if (privateKeyList != null && privateKeyList.size() > 0) {
            MerchantTradeCipher next = privateKeyList.listIterator().next();
            ali.setPrivateKey(next.getTradeToken());
        }
        // 支付宝 publicKey-202
        List<MerchantTradeCipher> publicKeyList = cipherGroup.get(CipherTypeEnum.ALIPAY_PUBLIC_KEY.getType());
        if (publicKeyList != null && publicKeyList.size() > 0) {
            MerchantTradeCipher next = publicKeyList.listIterator().next();
            ali.setPublicKey(next.getTradeToken());
        }
        // 支付宝 signType-203
        List<MerchantTradeCipher> signTypeList = cipherGroup.get(CipherTypeEnum.ALIPAY_SIGN_TYPE.getType());
        if (signTypeList != null && signTypeList.size() > 0) {
            MerchantTradeCipher next = signTypeList.listIterator().next();
            ali.setSignType(next.getTradeToken());
        }
        result.setAliParams(ali);
        // 设置微信支付参数
        WeixinPayConfigurationParams wxParams = new WeixinPayConfigurationParams();
        // 获取微信支付参数
        List<MerchantTradeCipher> wxAppidList = cipherGroup.get(CipherTypeEnum.WXPAY_APP_ID.getType());
        if (wxAppidList != null && wxAppidList.size() > 0) {
            MerchantTradeCipher next = wxAppidList.listIterator().next();
            wxParams.setWxpayAppid(next.getTradeToken());
        }
        List<MerchantTradeCipher> wxMchidList = cipherGroup.get(CipherTypeEnum.WXPAY_MCH_ID.getType());
        if (wxMchidList != null && wxMchidList.size() > 0) {
            MerchantTradeCipher next = wxMchidList.listIterator().next();
            wxParams.setWxpayMchid(next.getTradeToken());
        }
        List<MerchantTradeCipher> wxPrivateKeyList = cipherGroup.get(CipherTypeEnum.WXPAY_PRIVATE_KEY.getType());
        if (wxPrivateKeyList != null && wxPrivateKeyList.size() > 0) {
            MerchantTradeCipher next = wxPrivateKeyList.listIterator().next();
            wxParams.setWxpayPrivateKey(next.getTradeToken());
        }
        result.setWxParams(wxParams);
        // 设置参数签名校验token
        List<MerchantTradeCipher> tradeTokenList = cipherGroup.get(CipherTypeEnum.MERCHANT_TRADE_TOKEN.getType());
        if (tradeTokenList != null && tradeTokenList.size() > 0) {
            result.setMerchantTradeToken(tradeTokenList.listIterator().next().getTradeToken());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "transactionManager")
    public void doUpdateMerchantInfo(MerchanrUpdateQueryVo request) throws Exception {
        log.info("传入修改的商户详细信息：{}", JSON.toJSONString(request));
        Integer id = request.getId();
        String merchantId = request.getMerchantId();
        Merchant dbMerchant = commonService.get(id, Merchant.class);
        if (!dbMerchant.getMerchantId().equals(merchantId)) {
            throw new Exception("商户ID不符，请不要篡改数据");
        }
        // 判断当前要修改的商户信息是否是该用户所属商户
        AdmincenterOperator currentOpeartor = SecurityUtil.getCurrentOpeartor();
        String operatorSource = currentOpeartor.getOperatorSource();
        if (!operatorSource.toLowerCase().equals("hiapp") && !operatorSource.toLowerCase().equals(dbMerchant.getMerchantId().toLowerCase())) {
            throw new Exception("当前登录用户" + currentOpeartor.getOperatorName() + "无修改此数据的权限，请不要篡改数据");
        }
        // 用户校验通过 进行更新数据库操作
        dbMerchant.setIpWhiteList(request.getIpWhiteList());
        commonService.update(dbMerchant);
        // 获取到当前商户的所有密钥 匹配更新
        List<MerchantTradeCipher> cipherList = commonService.getList(MerchantTradeCipher.class, "merchantId", merchantId);
        Map<Integer, List<MerchantTradeCipher>> cipherGroup = cipherList.stream().collect(Collectors.groupingBy(MerchantTradeCipher::getTradeType));
        // 支付宝 appid-200
        List<MerchantTradeCipher> appidList = cipherGroup.get(CipherTypeEnum.ALIPAY_APP_ID.getType());
        if (appidList != null && appidList.size() > 0) {
            MerchantTradeCipher next = appidList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getAliAppid())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getAliAppid());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.ALIPAY_APP_ID.getType());
            cipher.setTradeToken(request.getAliAppid());
            commonService.save(cipher);
        }
        // 支付宝 privateKey-201
        List<MerchantTradeCipher> privateKeyList = cipherGroup.get(CipherTypeEnum.ALIPAY_PRIVATE_KEY.getType());
        if (privateKeyList != null && privateKeyList.size() > 0) {
            MerchantTradeCipher next = privateKeyList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getAliPrivateKey())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getAliPrivateKey());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.ALIPAY_PRIVATE_KEY.getType());
            cipher.setTradeToken(request.getAliPrivateKey());
            commonService.save(cipher);
        }
        // 支付宝 publicKey-202
        List<MerchantTradeCipher> publicKeyList = cipherGroup.get(CipherTypeEnum.ALIPAY_PUBLIC_KEY.getType());
        if (publicKeyList != null && publicKeyList.size() > 0) {
            MerchantTradeCipher next = publicKeyList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getAliPublicKey())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getAliPublicKey());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.ALIPAY_PUBLIC_KEY.getType());
            cipher.setTradeToken(request.getAliPublicKey());
            commonService.save(cipher);
        }
        // 支付宝 signType-203
        List<MerchantTradeCipher> signTypeList = cipherGroup.get(CipherTypeEnum.ALIPAY_SIGN_TYPE.getType());
        if (signTypeList != null && signTypeList.size() > 0) {
            MerchantTradeCipher next = signTypeList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getAliSignType())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getAliSignType());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.ALIPAY_SIGN_TYPE.getType());
            cipher.setTradeToken(request.getAliSignType());
            commonService.save(cipher);
        }

        // 微信支付 appid
        List<MerchantTradeCipher> wxAppidList = cipherGroup.get(CipherTypeEnum.WXPAY_APP_ID.getType());
        if (wxAppidList != null && wxAppidList.size() > 0) {
            MerchantTradeCipher next = wxAppidList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getWxpayAppid())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getWxpayAppid());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.WXPAY_APP_ID.getType());
            cipher.setTradeToken(request.getWxpayAppid());
            commonService.save(cipher);
        }


        // 微信支付 mchid
        List<MerchantTradeCipher> wxMchidList = cipherGroup.get(CipherTypeEnum.WXPAY_MCH_ID.getType());
        if (wxMchidList != null && wxMchidList.size() > 0) {
            MerchantTradeCipher next = wxMchidList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getWxpayMchid())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getWxpayMchid());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.WXPAY_MCH_ID.getType());
            cipher.setTradeToken(request.getWxpayMchid());
            commonService.save(cipher);
        }
        // ***********模拟回滚****************
        dbMerchant.setIpWhiteList(request.getIpWhiteList());
        if (request.getIpWhiteList().contains("error")) {
            throw new Exception("模拟错误");
        }
        // ***********模拟回滚****************
        // 微信支付 privatekey
        List<MerchantTradeCipher> wxPrivateKeyList = cipherGroup.get(CipherTypeEnum.WXPAY_PRIVATE_KEY.getType());
        if (wxPrivateKeyList != null && wxPrivateKeyList.size() > 0) {
            MerchantTradeCipher next = wxPrivateKeyList.listIterator().next();
            String tradeToken = next.getTradeToken();
            if (!Objects.equals(tradeToken, request.getWxpayPrivateKey())) {
                // 如果不相同，就更新一下下
                next.setTradeToken(request.getWxpayPrivateKey());
                commonService.update(next);
            }
        } else {
            // 目前没有这个记录 新增一条
            MerchantTradeCipher cipher = new MerchantTradeCipher();
            cipher.setMerchantId(merchantId);
            cipher.setTradeType(CipherTypeEnum.WXPAY_PRIVATE_KEY.getType());
            cipher.setTradeToken(request.getWxpayPrivateKey());
            commonService.save(cipher);
        }

    }

    @Override
    public List<Merchant> getAllMerchantList() {
        return commonService.getList(Merchant.class);
    }

    private static boolean chkStr(String... args) {
        for (String arg : args) {
            if (arg == null || arg.trim().equals("")) {
                return false;
            }
        }
        return true;
    }
}
