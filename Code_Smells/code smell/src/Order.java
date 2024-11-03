import java.util.List;

public class Order {
    private List<Item> items;
    private Customer customer;
    private String customerName;
    private String customerEmail;
    private boolean hasGiftCard;

    public Order(List<Item> items, Customer customer, boolean hasGiftCard) {
        this.items = items;
        this.customer = customer;
        this.customerName = customer.getName();
        this.customerEmail = customer.getEmail();
        this.hasGiftCard = hasGiftCard;
    }

    public double calculateTotalPrice() {
        double total = 0.0;
        for (Item item : items) {
            total += calculateItemNetPrice(item);
        }
        if (hasGiftCard) {
            total -= 10.0; // subtract $10 for gift card
        }
        if (total > 100.0) {
            total *= 0.9; // apply 10% discount for orders over $100
        }
        return total;
    }

    private double calculateItemNetPrice(Item item) {
        double price = item.getPrice();
        switch (item.getDiscountType()) {
            case PERCENTAGE:
                price -= item.getDiscountAmount() * price;
                break;
            case AMOUNT:
                price -= item.getDiscountAmount();
                break;
            default:
                // no discount
                break;
        }
        price = price * item.getQuantity();
        if (item instanceof TaxableItem taxableItem) {
            double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
            price += tax;
        }
        return price;
    }

    public void sendConfirmationEmail() {
        String message;
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Thank you for your order, " + customerName + "!\n\n" +
                "Your order details:\n");
        for (Item item : items) {
            messageBuilder.append(item.getName() + " - " + item.getPrice() + "\n");
        }
        messageBuilder.append("Total: " + calculateTotalPrice());
        message = messageBuilder.toString();
        EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
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

    public boolean hasGiftCard() {
        return hasGiftCard;
    }

    public void setHasGiftCard(boolean hasGiftCard) {
        this.hasGiftCard = hasGiftCard;
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