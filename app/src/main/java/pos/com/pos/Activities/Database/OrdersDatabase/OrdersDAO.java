package pos.com.pos.Activities.Database.OrdersDatabase;/**/

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao // this defines functions we can do on our db
public interface OrdersDAO {

    //Get Specific Order Information
    @Query("SELECT * FROM `order` WHERE order_no = (:order_num)")
    OrderEntry getOrderInfo(int order_num);


    @Query("SELECT * FROM `order` WHERE synced = 0")
    OrderEntry getAllUnsyncedOrders();

    @Query("SELECT * FROM `order`")
    OrderEntry[] getAllOrders();


    @Insert
    void insertOrder(OrderEntry orderEntry);

    @Update
    void updateOrder(OrderEntry ordrEntry);

    @Delete
    void deleteOrder(OrderEntry orderEntry);
    
}
