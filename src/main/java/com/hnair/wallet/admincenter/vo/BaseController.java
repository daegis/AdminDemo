package com.hnair.wallet.admincenter.vo;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/25 10:37
 */
public abstract class BaseController {

    private ThreadLocal<AJAXResult> results = new ThreadLocal<>();

    protected void start() {
        results.set(new AJAXResult());
    }

    protected AJAXResult end() {
        AJAXResult result = results.get();
        results.remove();
        return result;
    }

    protected void success( boolean flg ) {
        AJAXResult result = results.get();
        result.setSuccess(flg);
    }

    protected void success() {
        success(true);
    }

    protected void fail() {
        success(false);
    }

    protected void data( Object data ) {
        AJAXResult result = results.get();
        result.setData(data);
    }
}
