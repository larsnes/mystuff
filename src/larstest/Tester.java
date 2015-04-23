package larstest;

public class Tester {
    /**
     * Simple test program
     */
    public static void main(String[] args) {

        // Instantiate a new store
        Supermarket store = new Supermarket();
        int total = 0;

        try {
            // Add a few products, product B has special
            // bulk pricing rule: 5 for the price of 3
            store.addProduct("A", 20);
            store.addProduct("B", 50, 5, 3);
            store.addProduct("C", 30);

            // Exercise the checkout method
            total = store.checkout("A");
            System.out.println("total:  " + total);

            total = store.checkout("BBBB");
            System.out.println("total:  " + total);

            total = store.checkout("BBBBB");
            System.out.println("total:  " + total);

            total = store.checkout("ABBACBBAB");
            System.out.println("total:  " + total);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        try {
            // Remove a product to exercise exception handling
            store.removeProduct("C");
            total = store.checkout("ABBACBBAB");
            System.out.println("total:  " + total);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        try {
            // Add back with invalid bulk rule
            store.addProduct("C", 50, 5, 0);
            total = store.checkout("ABBACBBAB");
            System.out.println("total:  " + total);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        try {
            // Add back with invalid bulk count
            store.addProduct("C", 50, 0, 3);
            total = store.checkout("ABBACBBAB");
            System.out.println("total:  " + total);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}
