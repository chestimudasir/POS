package pos.com.pos.Activities.Models;

public class OrderModel {
    String order_type;
    int table_no;
    float order_price;

    OrderModel(){}

    public OrderModel(String order_type, int table_no, float order_price) {
        this.order_type = order_type;
        this.table_no = table_no;
        this.order_price = order_price;
    }
}
