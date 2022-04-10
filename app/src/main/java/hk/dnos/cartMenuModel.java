package hk.dnos;

import java.util.Date;

public class cartMenuModel {

    String menuPrice,menuQuantity,menuName;

    public cartMenuModel() {
    }

    public cartMenuModel(String menuPrice, String menuQuantity, String menuName) {
        this.menuPrice = menuPrice;
        this.menuQuantity = menuQuantity;
        this.menuName = menuName;
    }


    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuQuantity() {
        return menuQuantity;
    }

    public void setMenuQuantity(String menuQuantity) {
        this.menuQuantity = menuQuantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


}
