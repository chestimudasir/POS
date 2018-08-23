package pos.com.pos.Activities.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuDataBase;
import pos.com.pos.Activities.Database.OrdersDatabase.OrderEntry;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class OrdersFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ArrayList<pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem> menuItemList;


    //ORDER details that need to be global
    private int order_number;
    private RecyclerView.Adapter<ViewHolder> adapterGlobalTable;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView.Adapter<ViewHolderMenu> adapterGlobalMenu;
    //Arrays for menu String and menu Objects
    private String[] menuItems;
    private pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem[] menuItemsObjects;

    private int table_current;
    private float current_table_cost=0;

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

        //Init vars
        final DecimalFormat df = new DecimalFormat("#.##");
        menuItemList = new ArrayList<>();
        FirebaseAssistant.initFire(getActivity());


        //INIT BOTTOM SHEET
        final View bottomSheet = root.findViewById(R.id.bottomSheet);
        final View infoCard =root.findViewById(R.id.bottomSheetInfo);

        BottomSheetBehavior<View> infocard_behavior = BottomSheetBehavior.from(infoCard);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        infocard_behavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        final Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");

        //Price Number
        final TextView totalPrice = root.findViewById(R.id.total_price);
        totalPrice.setTypeface(custom_font);
        //By Default set bill to 0
        totalPrice.setText("₹ 0");

        //ORDERSING.. TITLE
        TextView orders = root.findViewById(R.id.orders);
        orders.setTypeface(custom_font);

        //Order Form Views Initiation
        final EditText cust_no = root.findViewById(R.id.editText8),
                marker = root.findViewById(R.id.editText4),
                discount = root.findViewById(R.id.editText7);

        final Button sync = root.findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAssistant syncManager = new FirebaseAssistant();
                syncManager.syncOrders();
            }
        });

        //Add to ordered Items and database
        RecyclerView menu_items_order = root.findViewById(R.id.rv_items);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        menu_items_order.setLayoutManager(linearLayoutManager);

        adapterGlobalMenu = new RecyclerView.Adapter<ViewHolderMenu>() {
            @NonNull
            @Override
            public ViewHolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolderMenu(LayoutInflater.from(getActivity()).inflate(R.layout.menu_items_order , parent , false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolderMenu holder, int position) {
                holder.nameof_item.setTypeface(custom_font);
                holder.nameof_item.setText(menuItemList.get(holder.getAdapterPosition()).item_name);
            }

            @Override
            public int getItemCount() {
                return menuItemList.size();
            }
        };
        menu_items_order.setAdapter(adapterGlobalMenu);


        //On Click menu Items and Auto Complete stuff
        final AutoCompleteTextView items = root.findViewById(R.id.editText6);
        //Get Menu
        initMenu();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.auto_complete, menuItems);

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem item = searchAndGet((String) parent.getItemAtPosition(position));
                menuItemList.add(item);
                adapterGlobalMenu.notifyDataSetChanged();

                //Calculate total price
                current_table_cost += item.item_price;
                current_table_cost += (18/current_table_cost);

                df.format(current_table_cost);
                totalPrice.setText("₹ " + current_table_cost);

                items.setText("");
            }
        });

        items.setAdapter(adapter);
        items.setThreshold(1);

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
                holder.cost_on_table.setTypeface(custom_font);

                //SET TABLE DATA after checking NPA
                if (getOrderDetails(holder.getAdapterPosition()) != null) {
                    holder.cost_on_table.setText("₹ " + (int) getOrderDetails(holder.getAdapterPosition()).getTicket_cost());

                }

                //Indicator Logic
                indicate(holder.indictor ,holder.getAdapterPosition());

                //On Click Expand and let the system know what table and what Order no
                //to work with
                holder.clickLayout.setOnClickListener(new View.OnClickListener() {
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

        //INITIATE ORDER PROCESS
        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If no discount set by default keep it as 0%
                if (discount.getText().toString().equals("")){
                    discount.setText("0");
                }
                //Do the magic and order and set the order as unsynched magically
                initOrderAndInsert(table_current ,
                        current_table_cost,
                        cust_no.getText().toString(),
                        "",
                        Integer.parseInt(discount.getText().toString()),
                        0,
                        0,
                        "");

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
                            float cost,
                            String cust_no,
                            String emp_sign,
                            int dcnt,
                            int settled,
                            int bal,
                            String marker){

        //Create Order Object
        final OrderEntry orderEntry = new OrderEntry();
        orderEntry.setBalance(bal);
        orderEntry.setCust_num(cust_no);
        orderEntry.setDate_time(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        orderEntry.setOrder_no(order_number);
        orderEntry.setDiscount(dcnt);
        orderEntry.setTable_no(table_no);
        orderEntry.setSynched(0);
        orderEntry.setTicket_cost(cost);
        orderEntry.setEmp_sign(emp_sign);
        orderEntry.setSettled_flag(0);
        orderEntry.setMarker(marker);

        //ADD Order to DB
        OrdersDatabase ordersDatabase = OrdersDatabase.getInstance(getActivity());
        ordersDatabase.ordersDao().insertOrder(orderEntry);



        //Set Current Order to respective table and refresh RV
        UserConfig config = new UserConfig();
        config.setOrderToTable(table_current, order_number);
        adapterGlobalTable.notifyItemChanged(table_current-1);
    }

    //Get order details based on order number
    OrderEntry getOrderDetails(int pos){
        UserConfig userConfig = new UserConfig();
         return OrdersDatabase.getInstance(getActivity()).ordersDao().getOrderInfo(userConfig.getOrderNumberFromTable(pos+1));
    }

    void indicate(View indicator , int position){

        OrderEntry order = getOrderDetails(position);

        if (order!=null&&order.getSettled_flag()==0){
            indicator.setVisibility(View.VISIBLE);
        }else if (order == null){
            indicator.setVisibility(View.GONE);

        }
    }

    void initMenu(){

        menuItemsObjects =
                MenuDataBase.getInstance(getActivity()).MenuDOA().getMenu();


        menuItems = new String[menuItemsObjects.length];

        for (int i = 0; i < menuItemsObjects.length ; i++) {
            menuItems[i] = menuItemsObjects[i].item_name;
        }

    }

    pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem searchAndGet(String name){
        for (pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem item :
                menuItemsObjects){
            if (item.item_name.equals(name)){
                return item;
            }
        }
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView table_no;
        public TextView cost_on_table;
        public RelativeLayout clickLayout;
        public View indictor;
        public ViewHolder(View itemView) {
            super(itemView);
            indictor = itemView.findViewById(R.id.indicator);
            clickLayout = itemView.findViewById(R.id.clickacble_order);
            table_no = itemView.findViewById(R.id.textView14);
            cost_on_table = itemView.findViewById(R.id.textView15);

        }
    }

    class ViewHolderMenu extends RecyclerView.ViewHolder{

        public  TextView nameof_item;
        public ViewHolderMenu(View itemView) {
            super(itemView);
            nameof_item= itemView.findViewById(R.id.name_of_item_menu);
        }
    }



}
