package pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "menu")
public class MenuItem {

    @PrimaryKey (autoGenerate = true) @NonNull
    public
    int item_no;
    //Columns for the DB
    @ColumnInfo (name = "item_name")
    public
    String item_name;
    @ColumnInfo (name = "item_price")
    public
    int item_price;
    @ColumnInfo (name = "item_discount")
    public
    int item_discount;
    @ColumnInfo (name = "category")
    public
    String category;
    @ColumnInfo (name = "serve")
    public
    int serve;
    @ColumnInfo (name = "synced")
    public
    int synced;
    
    public MenuItem() {
    }
    
    public MenuItem( String item_name, int item_price, int item_discount, String category, int serve, int synced) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_discount = item_discount;
        this.category = category;
        this.serve = serve;
        this.synced = synced;
    }

    @NonNull
    public int getItem_no() {
        return item_no;
    }

    public void setItem_no(@NonNull int item_no) {
        this.item_no = item_no;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public int getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(int item_discount) {
        this.item_discount = item_discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getServe() {
        return serve;
    }

    public void setServe(int serve) {
        this.serve = serve;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}