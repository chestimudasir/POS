package pos.com.pos.Activities.Database.OrdersDatabase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
