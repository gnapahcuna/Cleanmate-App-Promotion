package CustomItem;

public class StateVOCust {
    private String customerName;
    private int customerID;
    private boolean selected;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String title) {
        this.customerName = title;
    }
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int title) {
        this.customerID = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
