package main.frontend;

import main.backend.Controller;
import main.backend.DataValidation;
import main.backend.item.Item;
import main.backend.member.Address;
import main.backend.member.Customer;
import main.backend.member.Member;
import main.backend.order.AreaDeliveryRateMap;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;
import main.backend.payment.Payment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class ConsoleUI {

    private final Scanner scanner;
    private final Controller controller;
    private final DataValidation dataValidation;
    // create immutable object to reduce bug occurrence
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.controller = new Controller();
        this.dataValidation = new DataValidation();
    }

    public Scanner getScanner() { return this.scanner; }

    public void closeScanner() { this.scanner.close(); }

    public void start() {
        while(true) {
            final Customer customer = this.getUser();
            if (customer == null) {
                this.closeScanner();
                return;
            }
            if (customer.isMember()) {
                this.startProcess((Member)customer);
            } else {
                this.startProcess(customer);
            }
        }
    }

    //buy
    private Payment.PAYMENT_TYPE askPaymentType() {
        while (true) {
            System.out.print("Please choose a payment type (C/c - Credit Card || O/o - Online Payment): ");
            final String paymentType = this.scanner.nextLine();
            final Payment.PAYMENT_TYPE payment_type = this.dataValidation.getPaymentType(paymentType);
            if (payment_type != null) {
                return payment_type;
            }
            System.out.println("Please enter C/c for Credit Card OR O/o for Online Payment only");
        }
    }



    //get Item ID
    private Item askItemID() {
        while (true) {
            System.out.print("Please enter the ID of the item you wish to order: ");
            final String itemID = this.scanner.nextLine();
            try {
                final int ID = Integer.parseInt(itemID);
                final Item item = this.getItem(ID);
                if (item != null) {
                    return item;
                }
                System.out.println("Please enter valid ID");
            } catch (final NumberFormatException e) {
                System.out.println("Please enter valid ID");
            }
        }
    }

    private Item getItem(final int ID) { return this.controller.searchItemByID(ID); }

    //get Item Quantity
    private int askQuantity() {
        while (true) {
            System.out.print("Please enter the quantity of the item you wish to order: ");
            final String quantity = this.scanner.nextLine();
            if (DataValidation.tryParseIntInRange(quantity, 1, Integer.MAX_VALUE)) {
                return Integer.parseInt(quantity);
            }
            System.out.println("Please enter positive integer only");
        }
    }

    //terminate Order
    private String continueOrdering() {
        System.out.print("Would you like to continue ordering? (Press Enter - Continue Ordering || 1 - Cancel Order and Back to Menu || 2 - Confirm Order): ");
        return this.scanner.nextLine();
    }

    private Order.ORDER_STATUS payNowOrLater() {
        while(true) {
            System.out.print("Please choose pay now(n/N) or later(l/L): ");
            final String choice = this.scanner.nextLine();
            final Order.ORDER_STATUS orderStatus = this.dataValidation.getOrderStatus(choice);
            if (orderStatus != null) {
                return orderStatus;
            }
            System.out.println("Please enter (n/N) to pay now or (l/L) to pay later");
        }
    }

    private boolean confirmPayPendingOrder() {
        while(true) {
            System.out.print("Request confirmation to pay the order (1 - Confirm || 0 - Deny): ");
            switch (this.scanner.nextLine()) {
                case "1":
                    return true;
                case "0":
                    return false;
                default:
                    break;
            }
        }
    }

    private boolean confirmCancelPendingOrder() {
        while(true) {
            System.out.print("Request confirmation to cancel the order (1 - Confirm || 0 - Deny): ");
            switch (this.scanner.nextLine()) {
                case "1":
                    return true;
                case "0":
                    return false;
            }
        }
    }

    private long askOrderID() {
        while(true) {
            System.out.print("Please enter the order ID of the order: ");
            try {
                return Long.parseLong(this.scanner.nextLine());
            } catch (final NumberFormatException e) {
                System.out.println("Only positive integers are allowed: ");
            }
        }
    }

    private Member createMember() {

        final Member memberWithLatestID = this.controller.getMemberLargestID();
        final int ID = memberWithLatestID != null ? memberWithLatestID.getRegisteredNumber() + 1 : 180000;

        return new Member(this.askName(),
                this.askPassword(), ID,
                this.askAddress(),
                this.askContact());
    }

    private Customer createCustomer() { return new Customer(this.askName(), this.askAddress()); }

    private String askContact() {
        while (true) {
            System.out.print("Please enter your phone number (+601): ");
            final String contact = this.scanner.nextLine();
            if (this.dataValidation.isContactCorrect(contact)) {
                return "01" + contact;
            }
            System.out.println("Please enter proper format of contact number");
        }
    }

    private String askName() {
        while (true) {
            System.out.print("Please enter your name: ");
            final String name = this.scanner.nextLine();
            if (this.dataValidation.isProperStringFormat(name)) {
                return name;
            }
            System.out.println("Please enter proper format of name");
        }
    }

    private String askPassword() {
        while (true) {
            System.out.print("Please enter your password: ");
            final String password = this.scanner.nextLine();
            if (password.isEmpty() || password.isBlank()) {
                System.out.println("Password cannot be empty/blank");
            }
            else {
                System.out.print("Please confirm your password: ");
                final String confirmPassword = this.scanner.nextLine();
                if (password.equals(confirmPassword)) {
                    return password;
                } else {
                    System.out.println("Password do not match. Please try again");
                }
            }
        }
    }

    //For main menu
    private enum CHOICE {
        LOGIN {
            @Override
            Customer getUser(final ConsoleUI consoleUI) {
                return consoleUI.findMember();
            }
            @Override
            public String toString() { return "1"; }
        }, REGISTER_MEMBER {
            @Override
            Customer getUser(final ConsoleUI consoleUI) {
                final Member member = consoleUI.createMember();
                consoleUI.controller.addMember(member);
                return member;
            }
            @Override
            public String toString() { return "2"; }
        }, CONTINUE_GUEST {
            @Override
            Customer getUser(final ConsoleUI consoleUI) {
                return consoleUI.createCustomer();
            }
            @Override
            public String toString() { return "3"; }
        }, EXIT {
            @Override
            Customer getUser(final ConsoleUI consoleUI) { return null; }
            @Override
            public String toString() { return "4"; }
        };
        abstract Customer getUser(final ConsoleUI consoleUI);
    }

    private Customer getUser() {
        System.out.println("\n+-----------------------------------------------+");
        System.out.println("| Welcome to Mrs Kiah Homemade Cakes & Pastries |");
        System.out.println("+-----------------------------------------------+");
        System.out.println("| 1. Login                                      |");
        System.out.println("| 2. Register as member                         |");
        System.out.println("| 3. Continue as guest                          |");
        System.out.println("| 4. Exit                                       |");
        System.out.println("+-----------------------------------------------+");
        return this.askUserChoice().getUser(this);
    }

    private Member findMember() {
        while (true) {
            System.out.print("Please enter your ID: ");
            final String ID = this.scanner.nextLine();
            System.out.print("Please enter your password: ");
            final String password = this.scanner.nextLine();
            if (DataValidation.tryParseIntInRange(ID, 0, Integer.MAX_VALUE)) {
                final int integerID = Integer.parseInt(ID);
                final Member member = this.getMember(integerID, password);
                if (member != null) {
                    return member;
                }
                System.out.println("Invalid ID and/or password. Please try again");
            }
        }
    }

    private Member getMember(final int integerID, final String password) { return this.controller.searchMember(integerID, password); }

    private CHOICE askUserChoice() {
        while(true) {
            System.out.print("Please enter your choice (1-4): ");
            final String choice = this.scanner.nextLine();
            final CHOICE choice1 = this.getChoice(choice);
            if (choice1 != null) {
                return choice1;
            }
            System.out.println("Please enter from 1 to 4 only");
        }
    }

    private CHOICE getChoice(final String choice) { return Arrays.stream(CHOICE.values()).filter(choice1 -> choice.equals(choice1.toString())).findAny().orElse(null); }

    //ask address
    private Address askAddress() {
        return new Address("Melaka",
                this.askDistrict(),
                this.askArea(),
                this.askStreet(),
                this.askPostalCode(),
                this.askHouseNumber());
    }

    private String askArea() {
        while(true) {
            System.out.print("Please enter your area (Eg. Alor Gajah): ");
            final String area = this.scanner.nextLine();
            if (this.dataValidation.isAreaCorrect(area, this.controller.getAreaDeliveryRateMap().getAreaDeliveryRateMap())) {
                return area;
            }
            System.out.println("Invalid area entered");
        }
    }

    private String askDistrict() {
        while(true) {
            System.out.print("Please enter your district (Eg. Alor Gajah, Melaka Tengah) : ");
            final String district = this.scanner.nextLine();
            if (this.dataValidation.isDistrictCorrect(district)) {
                return district;
            }
            System.out.println("Please choose either Alor Gajah, Melaka Tengah(Central Melaka) OR Jasin");
        }
    }

    private String askStreet() {
        while(true) {
            System.out.print("Please enter your street name (Eg. Street Long): ");
            final String street = this.scanner.nextLine();
            if (this.dataValidation.isProperStringFormat(street)) {
                return street;
            }
            System.out.println("Please enter alphabets and space only");
        }
    }

    private int askPostalCode() {
        while(true) {
            System.out.print("Please enter your postal code (Eg. 10134) : ");
            final String postalCode = this.scanner.nextLine();
            if (DataValidation.tryParseIntInRange(postalCode, 10000, 99999)) {
                return Integer.parseInt(postalCode);
            }
            System.out.println("Please enter only 5 digit number");
        }
    }

    private int askHouseNumber() {
        while(true) {
            System.out.print("Please enter your house number (Eg. 123, 98, 456): ");
            final String postalCode = this.scanner.nextLine();
            if (DataValidation.tryParseIntInRange(postalCode, 1, Integer.MAX_VALUE)) {
                return Integer.parseInt(postalCode);
            }
            System.out.println("Please enter positive integer only");
        }
    }

    //average 10 digits
    private long askAccountNumber() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your account number: ");
            final String number = scanner.nextLine();
            if (DataValidation.tryParseLongInRange(number, 1000000000, 9999999999L)) {
                return Long.parseLong(number);
            }
            System.out.println("Invalid format. Please enter 10 digits of online accoutn number only");
        }
    }
    //average 16 digits
    private long askCreditCard() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your credit-card number: ");
            final String number = scanner.nextLine();
            if (DataValidation.tryParseLongInRange(number, 1000000000000000L, 9999999999999999L)) {
                return Long.parseLong(number);
            }
            System.out.println("Invalid format. Please enter 16 digits of credit card number only");
        }
    }

    private void viewItems(final List<Item> itemList) {
        final String header = String.format("%-12s%-36s%-8s%-17s%-22s%-13s", "Item ID", "Item", "Type", "Member Price(RM)", "Non-Member Price(RM)", "Promotional");
        System.out.println(header + "\n" + "-".repeat(110));
        itemList.forEach(item -> System.out.println(item.toString()));
        System.out.println();
    }


    private void startProcess(final Customer customer) {
        while(true) {
            System.out.println("+-------------------------+");
            System.out.println("| Please choose an option |");
            System.out.println("+-------------------------+");
            System.out.println("| 1. View Items           |");
            System.out.println("| 2. Order Items          |");
            System.out.println("| 3. View Orders          |");
            System.out.println("| 4. Exit                 |");
            System.out.println("+-------------------------+");
            System.out.print("Please enter your choice: ");
            switch (this.scanner.nextLine()) {
                case "1":
                    this.viewItems(controller.getItemsRecord().getDataList());
                    break;
                case "2":
                    this.viewItems(controller.getItemsRecord().getDataList());
                    final Order order = this.makeOrder(customer, controller.getAreaDeliveryRateMap());
                    if (order != null) {
                        this.show(order);
                        final Payment.PAYMENT_TYPE payment_type = this.askPaymentType();
                        final long number;
                        if (payment_type == Payment.PAYMENT_TYPE.ONLINE_PAYMENT) {
                            number = this.askAccountNumber();
                        } else {
                            number = this.askCreditCard();
                        }
                        final Payment payment = customer.createPayment(order, payment_type, number);
                        System.out.println(payment.receipt());
                        this.controller.addOrder(order);
                    } else {
                        System.out.println("Order cancelled");
                    }
                    break;
                case "3":
                    final List<Order> customerOrderList = this.controller.getOrderRecord().getOrderList()
                            .stream().filter(
                                    memberOrder -> customer.getRegisteredNumber() == memberOrder.getRegisteredNumber())
                            .collect(Collectors.toUnmodifiableList());
                    this.viewOrders(customerOrderList);
                    break;
                case "4":
                    return;
                default:
                    break;
            }
        }
    }

    private Order makeOrder(final Customer customer, final AreaDeliveryRateMap areaDeliveryRateMap) {
        final OrderCartMap itemsOrdered = new OrderCartMap();
        while(true) {
            final Item item = this.askItemID();
            final int quantity = this.askQuantity();
            itemsOrdered.put(item, quantity);
            switch (this.continueOrdering()) {
                case "2":
                    if (customer.isMember()) {
                        return ((Member)customer).createOrder(itemsOrdered, this.payNowOrLater(), areaDeliveryRateMap);
                    } else {
                        return customer.createOrder(itemsOrdered, areaDeliveryRateMap);
                    }
                case "1":
                    return null;
            }
        }
    }

    private void startProcess(final Member member) {
        while(true) {
            System.out.println("+--------------------------+");
            System.out.println("| Please choose an option  |");
            System.out.println("+--------------------------+");
            System.out.println("| 1. View Items            |");
            System.out.println("| 2. Order Items           |");
            System.out.println("| 3. View Order History    |");
            System.out.println("| 4. Pay pending order     |");
            System.out.println("| 5. Cancel pending order  |");
            System.out.println("| 6. Exit                  |");
            System.out.println("+--------------------------+");
            System.out.print("Please enter your choice: ");
            switch (this.getScanner().nextLine()) {
                case "1":
                    this.viewItems(this.controller.getItemsRecord().getDataList());
                    break;
                case "2":
                    this.viewItems(this.controller.getItemsRecord().getDataList());
                    final Order order = this.makeOrder(member, this.controller.getAreaDeliveryRateMap());
                    if (order != null) {
                        this.show(order);
                        if (order.getOrderStatus() == Order.ORDER_STATUS.SUCCESSFUL) {
                            System.out.println("\n\n**If you quit/cancel right now, the order will be automatically cancelled as well**\n\n");
                            final Payment.PAYMENT_TYPE payment_type = this.askPaymentType();
                            final long number;
                            if (payment_type == Payment.PAYMENT_TYPE.ONLINE_PAYMENT) {
                                number = this.askAccountNumber();
                            } else {
                                number = this.askCreditCard();
                            }
                            final Payment payment = member.createPayment(order, payment_type, number);
                            this.controller.addOrder(order);
                            System.out.println(payment.receipt());
                        } else {
                            this.controller.addOrder(order);
                        }
                    } else {
                        System.out.println("Order cancelled");
                    }
                    break;
                case "3":
                    final List<Order> memberOrderList = this.controller.getOrderRecord().getOrderList()
                            .stream().filter(
                                    memberOrder -> member.getRegisteredNumber() == memberOrder.getRegisteredNumber())
                            .collect(Collectors.toUnmodifiableList());
                    this.viewOrders(memberOrderList);
                    break;
                case "4":
                    this.payPendingOrder(member);
                    break;
                case "5":
                    this.cancelPendingOrder(member);
                    break;
                case "6":
                    return;
                default:
                    break;
            }
        }
    }


    private void payPendingOrder(final Member member) {
        final List<Order> pendingOrderList = this.controller.getOrderRecord().getOrderList()
                .stream().filter(
                order -> order.getOrderStatus() == Order.ORDER_STATUS.PENDING
                        && member.getRegisteredNumber() == order.getRegisteredNumber())
                .collect(Collectors.toUnmodifiableList());

        if (pendingOrderList.isEmpty()) {
            System.out.println("There are no pending order");
            return;
        }

        this.viewOrders(pendingOrderList);
        final Order pendingOrder = this.getOrderFromOrderList(pendingOrderList);
        if (this.confirmPayPendingOrder()) {
            pendingOrder.changePendingToSuccess();
            final Payment.PAYMENT_TYPE payment_type = this.askPaymentType();
            final long number;
            if (payment_type == Payment.PAYMENT_TYPE.ONLINE_PAYMENT) {
                number = this.askAccountNumber();
            } else {
                number = this.askCreditCard();
            }
            final Payment payment = member.createPayment(pendingOrder, payment_type, number);
            System.out.println("Payment for this pending order has been paid");
            System.out.println(payment.receipt());
        }
    }

    private void cancelPendingOrder(final Member member) {
        final List<Order> pendingOrderList = this.controller.getOrderRecord().getOrderList()
                .stream().filter(
                        order -> order.getOrderStatus() == Order.ORDER_STATUS.PENDING
                                && member.getRegisteredNumber() == order.getRegisteredNumber())
                .collect(Collectors.toUnmodifiableList());

        if (pendingOrderList.isEmpty()) {
            System.out.println("There are no pending order");
            return;
        }

        this.viewOrders(pendingOrderList);
        final Order pendingOrder = this.getOrderFromOrderList(pendingOrderList);
        if (this.confirmCancelPendingOrder()) {
            this.controller.removeOrder(pendingOrder);
            System.out.println("Pending order has been cancelled");
        } else {
            System.out.println("Cancel pending order process has been cancelled and removed");
        }
    }

    private Order getOrderFromOrderList(final List<Order> orderList) {
        while(true) {
            final long ID = this.askOrderID();
            final Order orderRetrieved = orderList
                    .stream()
                    .filter(order -> order.getOrderID() == ID)
                    .findFirst()
                    .orElse(null);
            if (orderRetrieved != null) {
                return orderRetrieved;
            }
            System.out.println("The order ID you've entered does not exist");
        }
    }

    private void viewOrders(final List<Order> orderList) {
        if (orderList.isEmpty()) {
            System.out.println("You do not have any orders");
            return;
        }
        final String header = String.format("%-15s%-12s%-36s%-8s%-17s%-22s%-13s%-20s%-12s",
                "Order ID",
                "Item ID",
                "Item",
                "Type",
                "Member Price(RM)",
                "Non-Member Price(RM)",
                "Promotional",
                "Quantities Ordered",
                "Order Status");
        System.out.println("\n" + header + "\n" + "-".repeat(160));
        orderList.forEach(order -> System.out.println(this.formPrettyInfo(order)));
    }


    private String formPrettyInfo(final Order order) {
        final StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (final Map.Entry<Item, Integer> entryItem : order.getItemsOrdered().entrySet()) {
            i++;
            final String details = String.format("%-15s", order.getOrderID()) +
                    entryItem.getKey().toString() +
                    String.format("%-20s", entryItem.getValue()) +
                    String.format("%-12s", order.getOrderStatus());
            if (i < order.getNumberOfDifferentItemsOrdered()) {
                stringBuilder.append(details).append("\n");
            } else {
                stringBuilder.append(details);
            }
        }
        return stringBuilder.toString();
    }

    private void show(final Order order) {
        final String header = String.format("%-15s%-12s%-36s%-8s%-17s%-22s%-13s%-20s%-12s",
                "Order ID",
                "Item ID",
                "Item",
                "Type",
                "Member Price(RM)",
                "Non-Member Price(RM)",
                "Promotional",
                "Quantities Ordered",
                "Order Status");
        System.out.println("\n" + header + "\n" + "-".repeat(160));
        System.out.println(this.formPrettyInfo(order) + "\n");
        if (order.getOrderStatus() == Order.ORDER_STATUS.SUCCESSFUL) {
            System.out.println("Your total ordered price is RM "
                    + String.format("%.02f", order.getTotalPrice()) + " together with delivery charge of RM "
                    + String.format("%.02f", order.getDeliveryCharge()) + "\n");
        }
    }
}