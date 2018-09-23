package pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase;/**/

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao // this defines functions we can do on our db
public interface MenuDOA {

    //Get Specific Order Information
    @Query("SELECT * FROM `menu` WHERE item_price = (:order_num)")
    MenuItem getOrderInfo(int order_num);


    @Query("SELECT * FROM `menu` WHERE synced = 0")
    MenuItem[] getAllUnsyncedOrders();

    @Query("SELECT * FROM `menu`")
    MenuItem[] getMenu();


    @Insert
     void insertOrder(MenuItem orderEntry);

    @Update
    void updateMenu(MenuItem ordrEntry);

    @Delete
    void deleteOrder(MenuItem orderEntry);

    @Query("DELETE FROM `menu`")
    void deleteAll();
}
