package org.example.serverlogic;

public class CategoryOfGoods {

    protected String category;
    protected String goods;

    public String getCategory() {
        return category;
    }

    public String getGoods() {
        return goods;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "CategoryOfGoods{" +
                "category='" + category + '\'' +
                ", goods='" + goods + '\'' +
                '}';
    }
}
