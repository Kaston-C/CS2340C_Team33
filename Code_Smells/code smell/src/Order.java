import java.util.List;

public class Order {
    private List<Item> items;
    private Customer customer;

    public Order(List<Item> items, Customer customer) {
        this.items = items;
        this.customer = customer;
    }

    public double calculateTotalPrice() {
        double total = 0.0;
        for (Item item : items) {
            total += calculateItemNetPrice(item);
        }
        if (hasGiftCard()) {
            total -= 10.0; // subtract $10 for gift card
        }
        if (total > 100.0) {
            total *= 0.9; // apply 10% discount for orders over $100
        }
        return total;
    }

    private double calculateItemNetPrice(Item item) {
        double price = item.getPrice();
        if (item.getDiscountType() == DiscountType.PERCENTAGE) {
            price -= item.getDiscountAmount() * price;
        } else if (item.getDiscountType() == DiscountType.AMOUNT) {
            price -= item.getDiscountAmount();
        }
        price = price * item.getQuantity();
        if (item instanceof TaxableItem taxableItem) {
            double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
            price += tax;
        }
        return price;
    }

    private boolean hasGiftCard() {
        for (Item item : items) {
            if (item instanceof GiftCardItem) {
                return true;
            }
        }
        return false;
    }

    public void sendConfirmationEmail() {
        System.out.println("Email to: " + customer.getEmail());
        System.out.println("Subject: Order Confirmation");
        System.out.println("Body: Thank you for your order, " + customer.getName() + "!\n\n");
        printOrder();
        System.out.println("Total: " + calculateTotalPrice());
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(item.getName() + " - " + item.getPrice());
        }
    }

    public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
    }
}
