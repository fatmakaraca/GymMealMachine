import java.util.ArrayList;
import java.util.List;

public class Slot {
    private int slotOccupancy = 0;
    private int slotNumber;
    private String productNameInSlot = null;

    private Product product;

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public int getSlotOccupancy() {
        return slotOccupancy;
    }

    public String getProductNameInSlot() {
        return productNameInSlot;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int addProduct(String productNameInSlot) {
        this.productNameInSlot = productNameInSlot;

        slotOccupancy++;
        return 1;
    }

    public void reduceProduct(String productNameInSlot) {
        this.productNameInSlot = productNameInSlot;

        slotOccupancy--;
    }
}


