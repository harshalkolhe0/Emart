package hk.dnos;

import java.util.Date;

public class noteclass {
    public String menuName,menuPrice,menuQuantity;
    long total;
    public Date date;

    public noteclass(){

    }
    public noteclass(String menuMane, String menuPrice, String menuQuantity, long total) {
        this.menuName = menuMane;
        this.menuPrice = menuPrice;
        this.menuQuantity = menuQuantity;
        this.total = total;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public String getMenuQuantity() {
        return menuQuantity;
    }

    public String getTotal() {
        return String.valueOf(Integer.parseInt(menuPrice)*Integer.parseInt(menuQuantity));
    }

    public String getDate() {
        return String.valueOf(date);
    }
}

