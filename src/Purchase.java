public class Purchase {
    private String type;
    private int money;
    private String choice;
    private int value;
    private boolean invalidCoin;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int returnMoney(int productPrice){
        return (money - productPrice);
    }

    public void setInvalidCoin(boolean invalidCoin) {
        this.invalidCoin = invalidCoin;
    }

    public boolean isInvalidCoin() {
        return invalidCoin;
    }
}
