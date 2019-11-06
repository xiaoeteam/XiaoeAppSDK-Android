package com.xiaoeshopsdk.sample.net;

public class LoginEntity
{
    /**
     * code : 0
     * msg : 成功 hint:[2482]
     * data : {"token_key":"xe_sdk_token","token_value":"d6a09cd4c0111e92e0dd5594234e336d"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token_key : xe_sdk_token
         * token_value : d6a09cd4c0111e92e0dd5594234e336d
         */

        private String token_key;
        private String token_value;

        public String getToken_key() {
            return token_key;
        }

        public void setToken_key(String token_key) {
            this.token_key = token_key;
        }

        public String getToken_value() {
            return token_value;
        }

        public void setToken_value(String token_value) {
            this.token_value = token_value;
        }
    }
}
