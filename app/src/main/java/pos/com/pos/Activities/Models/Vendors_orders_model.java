package pos.com.pos.Activities.Models;

import java.util.ArrayList;

public class Vendors_orders_model {

    public String uid_vendor , ordered_at;
    public int priority;

    public String getUid_vendor() {
        return uid_vendor;
    }

    public void setUid_vendor(String uid_vendor) {
        this.uid_vendor = uid_vendor;
    }

    public String getOrdered_at() {
        return ordered_at;
    }

    public void setOrdered_at(String ordered_at) {
        this.ordered_at = ordered_at;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> items;

}
