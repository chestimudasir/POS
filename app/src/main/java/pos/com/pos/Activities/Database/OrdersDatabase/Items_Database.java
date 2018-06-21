package pos.com.pos.Activities.Database.OrdersDatabase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Order_Items.class}, version = 1, exportSchema = false)//we include the classes which represent our tables OrderEntry.class
public abstract class Items_Database extends RoomDatabase {
    private static final String LOG_TAG = Items_Database.class.getSimpleName();
    private static final Object LOCK =  new Object();
    private static final String DATABASE_NAME = "order_items";
    private static Items_Database sInstance;

    public static Items_Database getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){

                sInstance = Room.databaseBuilder(context,
                        Items_Database.class, Items_Database.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    // to add a order dao we will add an abstract function that returns it
    public abstract Items_DAO itemsDao();
}
