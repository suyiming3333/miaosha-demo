package com.sym.miaoshaodemo.domain;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: Good
 * @Package com.sym.miaoshaodemo.domain
 * @Description: TODO
 * @date 2019/5/4 17:25
 */

public class Good {
    private int id;
    private String name;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private int total;
}
