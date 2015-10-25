package com.zhaoqiang.shoppingstore.modle;

/**
 * Project_name : ShoppingStore
 * Author : zhaoQiang
 * Create_time : 2015/10/14 | 09:41
 */

/**
 * 购物车条目，用于描述购物车一个Item
 */
public class CartItem {
    /**
     * 商品ID
     */
    private long productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品单价
     */
    private float productPrice;
    /**
     * 商品图标
     */
    private String productIcon;
    //----------------------
    /**
     * 购买数量
     */
    private int Count;

    //TODO 考虑增加其他字段：

    /**
     * 代表购物车中  当前条目是否选中了checkbox
     */
    private boolean checked;



    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}




