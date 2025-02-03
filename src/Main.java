import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;


public class Main {
    /**
     * This is the entry point of the program. It takes command line arguments, reads food and purchase information, processes them, and writes the results to a file.
     *
     * @param args Command line arguments: input_file_1, input_file_2, output_file
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            return;
        }

        String inputFileName1 = args[0];
        String inputFileName2 = args[1];
        String outputFileName = args[2];
        try {
            FileWriter writer = new FileWriter(outputFileName);

            String[] contentProduct = FileInput.readFile(inputFileName1, false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
            String[] contentPurchase = FileInput.readFile(inputFileName2, false, false);
            FileOutput.writeToFile(outputFileName, "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.


            List<Slot> gmmSlots = loadingGMM(contentProduct, writer);

            purchasingOperations(gmmSlots, contentPurchase, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads food products and fills the GMM (Gym Meal Machine) slots.
     *
     * @param content The content of food products
     * @param writer  FileWriter object for file writing operation
     * @return List of filled GMM slots
     * @throws IOException Thrown when an error occurs during file writing operation
     */
    public static List<Slot> loadingGMM(String[] content, FileWriter writer) throws IOException {

        ArrayList<String> names = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < content.length; i++) {
            String[] line = content[i].split("\t");

            String searchName = line[0];
            String[] nutritionalValues = line[2].split(" ");
            int price = Integer.parseInt(line[1]);
            double protein = Double.parseDouble(nutritionalValues[0]);
            double carbohydrate = Double.parseDouble(nutritionalValues[1]);
            double fat = Double.parseDouble(nutritionalValues[2]); //The protein, carbohydrate, and fat values were chosen as double because they involve fractional numbers.

            boolean nameExists = false;
            //A loop was written to avoid adding the product again to the products array list if it has been added before.

            for (String name : names) {
                if (name.equals(searchName)) {
                    nameExists = true;
                    break;
                }
            }

            if (!nameExists) {
                names.add(searchName);
                products.add(new Product(searchName, price, protein, carbohydrate, fat));
            }
        }

        List<Slot> gmmSlots;

        gmmSlots = new ArrayList<>(); //gmmSlots will hold 24 slots.
        for (int i = 0; i < 24; i++) {
            gmmSlots.add(new Slot(i));
        }


        for (int i = 0; i < content.length; i++) {

            String[] line = content[i].split("\t");
            String currentProductName = line[0];  //The name of the product in the input file was assigned to the variable currentProductName.

            int fillResult = fill(gmmSlots, products, currentProductName, writer); //The fill function was called to fill the slots appropriately.
            if (fillResult == -1) { //If fillResult is equal to -1 because the machine is full, the slot filling process stops.
                break;
            }

        }

        printGMM(gmmSlots, writer); //The status of the gmm machine was written to the file.
        return gmmSlots;
    }

    /**
     * Attempts to place the specified product into the GMM.
     *
     * @param gmmSlots           List of GMM slots
     * @param products           List of products to be loaded
     * @param currentProductName Name of the product to be placed
     * @param writer             FileWriter object for file writing operation
     * @return Result of the placement operation
     * @throws IOException Thrown when an error occurs during file writing operation
     */
    public static int fill(List<Slot> gmmSlots, List<Product> products, String currentProductName, FileWriter writer) throws IOException {
        for (Product product : products) {
            if (product.getName().equals(currentProductName)) {
                Product currentProduct = product;
                boolean isThereSuitableSlot = false;
                boolean isMachineFull = true;

                for (int j = 0; j < 24; j++) {
                    Slot currentSlot = gmmSlots.get(j); //currentSlot refers to the j-th slot object in gmmSlots.
                    if (currentSlot.getSlotOccupancy() < 10) {
                        isMachineFull = false; //If the occupancy of any slot is less than 10, the machine is not full.
                    }
                    if (currentSlot.getSlotOccupancy() < 10 && currentProductName.equals(currentSlot.getProductNameInSlot()))
                    //If the same product exists in that slot and the occupancy of the slot is less than 10, the product is added.
                    {
                        currentSlot.addProduct(currentProductName);
                        currentSlot.setProduct(currentProduct);
                        isThereSuitableSlot = true; //If the product could be added, it means there is an available slot.
                        break;

                    } else if (currentSlot.getSlotOccupancy() == 0)
                    //If the if block didn't execute and there are empty slots available, the product is added.
                    {
                        currentSlot.addProduct(currentProductName);
                        currentSlot.setProduct(currentProduct);
                        isThereSuitableSlot = true; //If the product could be added, it means there is an available slot.
                        break;
                    }

                }
                if (isMachineFull == true) { //If the machine is full, an error message is printed, and -1 is returned.
                    writer.write("INFO: There is no available place to put " + currentProductName + "\n");
                    writer.write("INFO: The machine is full!\n");
                    return -1;

                } else if (isMachineFull == false && isThereSuitableSlot == false) {
                    //If the machine is not completely full but there is no suitable place for the product, an error message is printed.
                    writer.write("INFO: There is no available place to put " + currentProductName + "\n");
                    return 1;
                }
            }
        }

        return 1;
    }

    /**
     * Writes the content of filled GMM slots to a file.
     *
     * @param gmmSlots List of filled GMM slots
     * @param writer   FileWriter object for file writing operation
     * @throws IOException Thrown when an error occurs during file writing operation
     */
    public static void printGMM(List<Slot> gmmSlots, FileWriter writer) throws IOException {
        writer.write("-----Gym Meal Machine-----\n");

        for (int i = 0; i < gmmSlots.size(); i++) {

            if (gmmSlots.get(i).getProduct() != null && gmmSlots.get(i).getSlotOccupancy() != 0) {
                String productName = gmmSlots.get(i).getProduct().getName();
                double productCal = gmmSlots.get(i).getProduct().calculateCalorie();
                int roundedProductCal = (int) Math.round(productCal);
                writer.write(productName + "(" + roundedProductCal + "," + " " + gmmSlots.get(i).getSlotOccupancy() + ")___");
            } else {
                writer.write("___(0, 0)___");
            }
            if (i % 4 == 3) {
                writer.write("\n");
            }
        }

        writer.write("----------\n");

    }

    /**
     * Processes purchase operations.
     *
     * @param gmmSlots List of filled GMM slots
     * @param content  Content of purchase operations
     * @param writer   FileWriter object for file writing operation
     * @throws IOException Thrown when an error occurs during file writing operation
     */
    public static void purchasingOperations(List<Slot> gmmSlots, String[] content, FileWriter writer) throws IOException {
        List<Purchase> purchaseObjects = new ArrayList<>();

        for (int i = 0; i < content.length; i++) {
            String[] line = content[i].split("\t");
            String type = line[0];
            String[] moneys = line[1].split(" ");
            String choice = line[2];
            int value = Integer.parseInt(line[3]);

            String[] validCoins = {"1", "5", "10", "20", "50", "100", "200"};

            boolean invalidCoin = false;
            int money = 0;

            for (int j = 0; j < moneys.length; j++) { //The total amount of money given to the machine is calculated.
                boolean valid = false;
                for (int k = 0; k < validCoins.length; k++) {
                    if (moneys[j].equals(validCoins[k])) {
                        money += Integer.parseInt(moneys[j]);
                        valid = true;
                    }
                }
                if (!valid) {
                    invalidCoin = true;
                }
            }

            Purchase currentPurchase = new Purchase();
            currentPurchase.setType(type);
            currentPurchase.setMoney(money);
            currentPurchase.setChoice(choice);
            currentPurchase.setValue(value);
            currentPurchase.setInvalidCoin(invalidCoin);

            purchaseObjects.add(currentPurchase);

        }

        for (int j = 0; j < purchaseObjects.size(); j++) {
            Purchase currentPurchase = purchaseObjects.get(j);
            writer.write("INPUT: " + content[j] + "\n");

            if (currentPurchase.isInvalidCoin() == false) {
                processPurchase(gmmSlots, currentPurchase, writer); //The method responsible for the purchase transaction was called.
            } else {
                writer.write("INFO: There is a type of money that is not accepted.\n");
                processPurchase(gmmSlots, currentPurchase, writer);
            }
        }
        printGMM(gmmSlots, writer); //The final state of the machine is printed.

    }

    /**
     * Processes the specified purchase operation.
     *
     * @param gmmSlots        List of filled GMM slots
     * @param currentPurchase Purchase operation to be processed
     * @param writer          FileWriter object for file writing operation
     * @return List of processed GMM slots
     * @throws IOException Thrown when an error occurs during file writing operation
     */
    public static List<Slot> processPurchase(List<Slot> gmmSlots, Purchase currentPurchase, FileWriter writer) throws IOException { //tek ürünün satın alma işlemi
        String choice = currentPurchase.getChoice();
        double value = currentPurchase.getValue();
        int money = currentPurchase.getMoney();

        double productValue = 0; //A starting value of 0 was assigned to productValue since the value for each choice will be determined later.

        //The operations were structured based on whether the choice is a number or not.

        if (currentPurchase.getChoice().equals("NUMBER")) {
            if (0 <= currentPurchase.getValue() && currentPurchase.getValue() < gmmSlots.size()) {
                //If the entered number value corresponds to any slot, then the operations are carried out.
                if (gmmSlots.get(currentPurchase.getValue()).getProduct() != null && gmmSlots.get(currentPurchase.getValue()).getSlotOccupancy() > 0) {
                    //If a product has been loaded into the slot indicated by the entered number value and the occupancy of the slot is greater than 0, then the operations are carried out.
                    if (currentPurchase.getMoney() >= gmmSlots.get(currentPurchase.getValue()).getProduct().getPrice()) {
                        //If there is enough money for the product in the slot indicated by the entered number value, the purchase transaction takes place.
                        gmmSlots.get(currentPurchase.getValue()).reduceProduct(gmmSlots.get(currentPurchase.getValue()).getProduct().getName()); //When a product is purchased, one item is deducted from that slot.
                        writer.write("PURCHASE: You have bought one " + gmmSlots.get(currentPurchase.getValue()).getProduct().getName() + "\n");
                        writer.write("RETURN: Returning your change: " + currentPurchase.returnMoney(gmmSlots.get(currentPurchase.getValue()).getProduct().getPrice()) + " TL\n");
                    } else {
                        writer.write("INFO: Insufficient money, try again with more money.\n");
                        writer.write("RETURN: Returning your change: " + currentPurchase.getMoney() + " TL\n");
                    }
                } else {
                    writer.write("INFO: This slot is empty, your money will be returned.\n");
                    writer.write("RETURN: Returning your change: " + currentPurchase.getMoney() + " TL\n");
                }

            } else {
                writer.write("INFO: Number cannot be accepted. Please try again with another number.\n");
                writer.write("RETURN: Returning your change: " + currentPurchase.getMoney() + " TL\n");
            }

        }

        if (!currentPurchase.getChoice().equals("NUMBER")) { //If the choice is not  "NUMBER" (protein, carb, fat, calorie), then the operations are carried out.

            boolean isProductFound = false;
            boolean isMoneyEnough = false;
            for (int i = 0; i < gmmSlots.size(); i++) {

                if (gmmSlots.get(i).getProduct() != null && gmmSlots.get(i).getSlotOccupancy() > 0) { //If a product has been loaded and the slot occupancy is greater than 0

                    if ((currentPurchase.getChoice().equals("PROTEIN"))) {
                        productValue = gmmSlots.get(i).getProduct().getProtein();
                    } else if ((currentPurchase.getChoice().equals("CARB"))) {
                        productValue = gmmSlots.get(i).getProduct().getCarbohydrate();
                    } else if ((currentPurchase.getChoice().equals("FAT"))) {
                        productValue = gmmSlots.get(i).getProduct().getFat();
                    } else if ((currentPurchase.getChoice().equals("CALORIE"))) {
                        productValue = gmmSlots.get(i).getProduct().calculateCalorie();
                    }
                    //The product value is adjusted according to the choice variable.

                    if (value >= productValue && value - productValue <= 5) { //If the difference between the desired value and the value of the products in the slot is less than 5,
                        isProductFound = true;

                        if (money >= gmmSlots.get(i).getProduct().getPrice()) { //If the money entered into the gmm machine is sufficient to purchase the product,
                            gmmSlots.get(i).reduceProduct(gmmSlots.get(i).getProduct().getName()); //the product is purchased, so the quantity of the product in the slot is reduced.
                            writer.write("PURCHASE: You have bought one " + gmmSlots.get(i).getProduct().getName() + "\n");
                            writer.write("RETURN: Returning your change: " + currentPurchase.returnMoney(gmmSlots.get(i).getProduct().getPrice()) + " TL\n");
                            isMoneyEnough = true;
                            break;
                        }

                    } else if (productValue >= value && productValue - value <= 5) { //If the difference between the desired value and the value of the products in the slot is less than 5,
                        isProductFound = true;

                        if (money >= gmmSlots.get(i).getProduct().getPrice()) { //If the money entered into the gmm machine is sufficient to purchase the product,
                            isMoneyEnough = true;
                            gmmSlots.get(i).reduceProduct(gmmSlots.get(i).getProduct().getName()); //the product is purchased, so the quantity of the product in the slot is reduced.
                            writer.write("PURCHASE: You have bought one " + gmmSlots.get(i).getProduct().getName() + "\n");
                            writer.write("RETURN: Returning your change: " + currentPurchase.returnMoney(gmmSlots.get(i).getProduct().getPrice()) + " TL\n");
                            break;
                        }

                    }
                }
            }


            if (isProductFound == false) { //If a suitable product cannot be found, an error message is printed.
                writer.write("INFO: Product not found, your money will be returned.\n");
                writer.write("RETURN: Returning your change: " + currentPurchase.getMoney() + " TL\n");
            } else if (isProductFound == true && isMoneyEnough == false) { //If a suitable product is found but the money is not sufficient, an error message is printed.
                writer.write("INFO: Insufficient money, try again with more money.\n");
                writer.write("RETURN: Returning your change: " + currentPurchase.getMoney() + " TL\n");
            }
        }
        return gmmSlots;
    }

}


