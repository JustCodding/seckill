
package com.jbc.seckill.redis;

public class OrderKey extends BasePrefix {
    private OrderKey(String prefix) {
        super(prefix);
    }

   public static OrderKey getOrderByid = new OrderKey("orderid");

}
