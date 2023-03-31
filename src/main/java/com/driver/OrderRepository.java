package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    HashMap<String, List<String>> orderPartnerDb = new HashMap<>();


    public void addOrder(Order order) {
        orderDb.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderPartnerDb.containsKey(partnerId)) {
            List<String> l = orderPartnerDb.get(partnerId);
            l.add(orderId);

            orderPartnerDb.put(partnerId, l);
        }
        else {
            List<String> l = new ArrayList<>();
            l.add(orderId);

            orderPartnerDb.put(partnerId, l);
        }
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderPartnerDb.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderPartnerDb.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> l = new ArrayList<>();
        for(String s : orderDb.keySet()) {
            l.add(s);
        }
        return l;
    }

    public Integer getCountOfUnassignedOrders() {
        int count = 0;
        int assignedOrders = 0;

        for(List<String> l : orderPartnerDb.values()) {
            assignedOrders += l.size();
        }
        count = orderDb.size()-assignedOrders;
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int time1 = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(2));

        List<String> l = orderPartnerDb.get(partnerId);
        int count = 0;

        for(String s : l) {
            if(orderDb.get(s).getDeliveryTime() > time1) {
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> l = orderPartnerDb.get(partnerId);
        int time = orderDb.get(l.get(0)).getDeliveryTime();

        for(String s : l) {
            if(orderDb.get(s).getDeliveryTime() > time) {
                time = orderDb.get(s).getDeliveryTime();
            }
        }

        int minutes = time%60;
        int hour = (time-minutes)/60;
        String time1 = "" + hour + minutes;

        return time1;
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerDb.remove(partnerId);
        orderPartnerDb.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderDb.remove(orderId);

        for(List<String> l : orderPartnerDb.values()) {
            for(int i=0; i<l.size(); i++) {
                if(l.get(i).equals(orderId)) {
                    l.remove(i);
                    break;
                }
            }
        }
    }
}
