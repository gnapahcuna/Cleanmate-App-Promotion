package CustomItem;

public class StateVO {
    private String productName;
    private int productID;
    private boolean selected;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String title) {
        this.productName = title;
    }
    public int getProductID() {
        return productID;
    }

    public void setProductID(int title) {
        this.productID = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
