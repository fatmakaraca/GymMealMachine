import java.util.List;

public class Product {
    private String name;
    private int price;
    private double protein;
    private double carbohydrate;
    private double fat;
    private double calorie;

    public double calculateCalorie(){
        calorie = 4 * protein + 4 * carbohydrate + 9 * fat;
        return calorie;
    }

    public Product(String name, int price, double protein, double carbohydrate, double fat) {
        this.name = name;
        this.price = price;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getProtein() {
        return protein;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getFat() {
        return fat;
    }

}
