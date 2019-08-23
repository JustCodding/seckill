package com.jbc.seckill.vo;

import com.jbc.seckill.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class LoginVo {
    @NotNull
    @IsMobile //这个是自己 自定义的校验
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
