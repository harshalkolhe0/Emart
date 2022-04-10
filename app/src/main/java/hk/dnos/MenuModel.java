package hk.dnos;

public class MenuModel {

    String MenuName,MenuPrice;

    public MenuModel() {
        MenuName="asc";
        MenuPrice="234";
    }

    public MenuModel(String menuName, String menuPrice) {
        MenuName = menuName;
        MenuPrice = menuPrice;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getMenuPrice() {
        return MenuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        MenuPrice = menuPrice;
    }
}
