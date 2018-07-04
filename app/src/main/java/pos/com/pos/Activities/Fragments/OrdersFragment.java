package pos.com.pos.Activities.Fragments;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import pos.com.pos.Activities.Database.OrdersDatabase.OrderEntry;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.Activities.Models.OrderModel;
import pos.com.pos.R;

public class OrdersFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetBehavior infocard_behavior;
    ArrayList<OrderModel> orderModels = new ArrayList<>();

    //ORDER details that need to be global
    private int order_number;
    private RecyclerView.Adapter<ViewHolder> adapterGlobalTable;
    private int table_current;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        //INIT BOTTOM SHEET
        final View bottomSheet = root.findViewById(R.id.bottomSheet);
        final View infoCard =root.findViewById(R.id.bottomSheetInfo);

        infocard_behavior = BottomSheetBehavior.from(infoCard);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        infocard_behavior.setHideable(false);

        //ORDERSING.. TITLE
        TextView orders = root.findViewById(R.id.orders);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");
        orders.setTypeface(custom_font);


        //FOR ORDERS
        RecyclerView rv_orders = root.findViewById(R.id.order_view_rv);
        rv_orders.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapterGlobalTable= new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.order_card_holder , container , false));
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

                holder.table_no.setText("#" + (holder.getAdapterPosition() +1));

                //SET TABLE DATA after checking NPA
                if (getOrderDetails(holder.getAdapterPosition()) != null)
                    holder.cost_on_table.setText(""+getOrderDetails(holder.getAdapterPosition()).getOrder_no());



                //On Click Expand and let the system know what table and what Order no
                //to work with
                holder.table_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        initOrdering(holder.getAdapterPosition());
                    }
                });

            }

            @Override
            public int getItemCount() {
                return new UserConfig().getTableCount();
            }
        };

        rv_orders.setAdapter(adapterGlobalTable);
        //Get Orders


        //FOR TABLES
        RecyclerView rv = root.findViewById(R.id.tables);
        rv.setLayoutManager(new GridLayoutManager(getActivity() , 2));

        //DUMMUY TABLES in ORDER SHEET
        RecyclerView.Adapter<ViewHolder> viewHolderAdapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.table_layout,container ,false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            }

            @Override
            public int getItemCount() {
                return 7;
            }
        };

        rv.setAdapter(viewHolderAdapter);

        //INITIATE ORDER PROCESS
        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Do the magic and order and set the order as unsynched magically
                initOrderAndInsert(table_current ,
                        999,
                        "9596070982",
                        "",
                        10,
                        1,
                        0);
            }
        });

        //Handle Bottom Sheet Clicks
        root.findViewById(R.id.next).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new UserConfig().clear();
                return true;
            }
        });
        root.findViewById(R.id.new_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        BottomSheetBehavior.BottomSheetCallback callback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
             //       Toast.makeText(getActivity(), "Collapsed by dragging", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if (slideOffset == 0.0) {
              //      Toast.makeText(getActivity(), "Dragged down", Toast.LENGTH_SHORT).show();
                }
            }
        };

        bottomSheetBehavior.setBottomSheetCallback(callback);

        return root;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }

    //INIT
    void initOrdering(int pos){
        Random random = new Random();
        order_number = random.nextInt(10000-1)+1;
        table_current = pos+1;
    }

    //INSERTION AND LINKING
    @SuppressLint("StaticFieldLeak")
    void initOrderAndInsert(int table_no,
                            int cost,
                            String cust_no,
                            String emp_sign,
                            int dcnt,
                            int settled,
                            int bal){

        final OrdersDatabase orderModel = Room.databaseBuilder(getActivity() , OrdersDatabase.class , "OrdersDB").build();

        //Create Order Object
        final OrderEntry orderEntry = new OrderEntry();
        orderEntry.setBalance(bal);
        orderEntry.setCust_num(cust_no);
        orderEntry.setDate_time(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        orderEntry.setOrder_no(order_number);
        orderEntry.setDiscount(dcnt);
        orderEntry.setTable_no(table_no);
        orderEntry.setSynched(0);
        orderEntry.setTicket_cost(cost);
        orderEntry.setEmp_sign(emp_sign);
        orderEntry.setSettled_flag(1);

        //ADD Order to DB
        OrdersDatabase ordersDatabase = OrdersDatabase.getInstance(getActivity());
        ordersDatabase.ordersDao().insertOrder(orderEntry);

        //Set Current Order to respective table and refresh RV
        UserConfig config = new UserConfig();
        config.setOrderToTable(table_current, order_number);
        adapterGlobalTable.notifyDataSetChanged();
    }

    //Get order details based on order number
    OrderEntry getOrderDetails(int pos){
        UserConfig userConfig = new UserConfig();
         return OrdersDatabase.getInstance(getActivity()).ordersDao().getOrderInfo(userConfig.getOrderNumberFromTable(pos+1));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView table_no;
        public TextView cost_on_table;
        public ViewHolder(View itemView) {
            super(itemView);
            table_no = itemView.findViewById(R.id.textView14);
            cost_on_table = itemView.findViewById(R.id.textView15);
        }
    }

}
