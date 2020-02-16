package com.yt.base.lock;

public class GoodsInfo {

    private String productName;
    private int num;

    public GoodsInfo(String productName, int num) {
        this.productName = productName;
        this.num = num;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
