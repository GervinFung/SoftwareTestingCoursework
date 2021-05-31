package main.backend;

import main.backend.item.Item;
import main.backend.item.ItemList;
import main.backend.member.Member;
import main.backend.member.MemberList;
import main.backend.fileinterface.MyFileAccessor;
import main.backend.fileinterface.MyFileMutator;
import main.backend.order.AreaDeliveryRateMap;
import main.backend.order.Order;
import main.backend.order.OrderList;

import java.util.Comparator;

public final class Controller {

    private final AreaDeliveryRateMap areaDeliveryRateMap;
    private final MyFileMutator<Member> memberList;
    private final OrderList orderList;
    private final MyFileAccessor<Item> itemList;

    public Controller() {
        this.memberList = new MemberList();
        this.areaDeliveryRateMap = new AreaDeliveryRateMap();
        this.itemList = new ItemList();
        this.orderList = new OrderList();
    }

    public AreaDeliveryRateMap getAreaDeliveryRateMap() { return this.areaDeliveryRateMap; }
    public MyFileAccessor<Item> getItemsRecord() { return this.itemList; }
    public MyFileMutator<Member> getMemberRecord() { return this.memberList; }
    public OrderList getOrderRecord() { return this.orderList; }

    public Member searchMember(final int integerID, final String password) {
        return  this.memberList.getDataList().stream()
                .filter(member -> (member.getRegisteredNumber() == integerID && member.getPassword().equals(password)))
                .findFirst()
                .orElse(null);
    }

    public Member getMemberLargestID() { return this.memberList.getDataList().stream().max(Comparator.comparing(Member::getRegisteredNumber)).orElse(null); }
    public Item searchItemByID(final int ID) { return this.itemList.getDataList().stream().filter(item -> item.getID() == ID).findAny().orElse(null); }
    public void addMember(final Member member) { this.memberList.add(member); }
    public void removeMember(final Member member) { this.memberList.remove(member); }
    public void addOrder(final Order order) { this.orderList.add(order); }
    public void removeOrder(final Order order) { this.orderList.remove(order); }
}