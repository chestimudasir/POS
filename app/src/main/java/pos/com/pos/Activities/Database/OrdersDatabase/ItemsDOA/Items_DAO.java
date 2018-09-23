package pos.com.pos.Activities.Database.OrdersDatabase.ItemsDOA;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase.Order_Items;

import java.util.List;

@Dao
public interface Items_DAO {
    @Query("SELECT * FROM order_items ORDER BY (:orderNumber)")
    List<Order_Items> loadAlltasks(String orderNumber); // to populate whatever ui you have

    @Insert
    void insertTask(Order_Items orderItems);

    @Update
    void updateTask(Order_Items orderItems);

    @Delete
    void deleteTask(Order_Items orderItems);
}
