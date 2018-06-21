package pos.com.pos.Activities.Database.OrdersDatabase;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "order_items")
public class Order_Items {


    @ColumnInfo(name="items")
    private String items;

    @PrimaryKey(autoGenerate = true)
    private int orderNumber;

    public Order_Items(String items, int orderNumber) {
        this.items = items;
        this.orderNumber = orderNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
