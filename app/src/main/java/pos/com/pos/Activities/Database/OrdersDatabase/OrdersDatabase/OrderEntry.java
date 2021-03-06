package pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "order")
public class OrderEntry {
    
    @PrimaryKey (autoGenerate = false)@NonNull
    public
    int order_no;
    //Columns for the DB
    @ColumnInfo (name = "date_time")
    public
    String date_time;
    
    @ColumnInfo (name = "table_no")
    public
    int table_no;

    @ColumnInfo (name = "ticket_cost")
    public
    float ticket_cost;

    @ColumnInfo (name = "cust_num")
    public
    String cust_num;

    @ColumnInfo (name = "emp_sign")
    public
    String emp_sign;

    @ColumnInfo (name = "discount")
    public
    float discount;

    @ColumnInfo(name = "settled")
    public
    int settled_flag;

    @ColumnInfo(name = "balance")
    public
    int balance;

    //Might need
    @ColumnInfo (name = "tid")
    public
    String tid;

    @ColumnInfo (name = "synced")
    public
    int synched;
    @ColumnInfo (name = "marker")
    public
    String marker;

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public  OrderEntry(){}

    public OrderEntry(int order_no,
                      String date_time,
                      short table_no,
                      short ticket_cost,
                      String cust_num,
                      String emp_sign,
                      float discount,
                      byte settled_flag,
                      int balance,
                      String tid,
                      byte synched) {

        this.order_no = order_no;
        this.date_time = date_time;
        this.table_no = table_no;
        this.ticket_cost = ticket_cost;
        this.cust_num = cust_num;
        this.emp_sign = emp_sign;
        this.discount = discount;
        this.settled_flag = settled_flag;
        this.balance = balance;
        this.tid = tid;
        this.synched = synched;
    }

    public int getSynched() {
        return synched;
    }

    public void setSynched(int synched) {
        this.synched = synched;
    }


    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getTable_no() {
        return table_no;
    }

    public void setTable_no(int table_no) {
        this.table_no = table_no;
    }

    public float getTicket_cost() {
        return ticket_cost;
    }

    public void setTicket_cost(float ticket_cost) {
        this.ticket_cost = ticket_cost;
    }

    public String getCust_num() {
        return cust_num;
    }

    public void setCust_num(String cust_num) {
        this.cust_num = cust_num;
    }

    public String getEmp_sign() {
        return emp_sign;
    }

    public void setEmp_sign(String emp_sign) {
        this.emp_sign = emp_sign;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getSettled_flag() {
        return settled_flag;
    }

    public void setSettled_flag(int settled_flag) {
        this.settled_flag = settled_flag;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
