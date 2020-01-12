package com.project.common;

/**
 * 购物车商品是否选中枚举类
 */

public enum CheckEnum {
    CART_PRODUCT_CHECK(1,"已选中"),
    CART_PRODUCT_UNCHECK(0,"未选中")
    ;
    private int check;
    private String desc;

    CheckEnum(int check, String desc){
        this.check=check;
        this.desc=desc;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
