package com.yt.base.container;

import java.math.BigDecimal;

public class OrderData {

    private Long orderId;
    private BigDecimal orderPrice;

    public OrderData(Long orderId, BigDecimal orderPrice) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "orderId=" + orderId +
                ", orderPrice=" + orderPrice +
                '}';
    }
}
