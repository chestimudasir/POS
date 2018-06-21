package pos.com.pos.Activities.Fragments;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import pos.com.pos.Activities.Database.OrdersDatabase.OrderEntry;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.Activities.Models.OrderModel;
import pos.com.pos.R;

public class OrdersFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    BottomSheetBehavior bottomSheetBehavior;
    ArrayList<OrderModel> orderModels = new ArrayList<>();

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

        final View bottomSheet = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);

        TextView orders = root.findViewById(R.id.orders);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");
        orders.setTypeface(custom_font);


        //FOR ORDERS
        RecyclerView rv_orders = root.findViewById(R.id.order_view_rv);
        rv_orders.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.order_card_holder , container , false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.table_no.setText("#" + (holder.getAdapterPosition() +1));

                holder.table_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                });



            }

            @Override
            public int getItemCount() {
                return new UserConfig().getTableCount();
            }
        };

        rv_orders.setAdapter(adapter);
        //Get Orders


        //FOR TABLES
        RecyclerView rv = root.findViewById(R.id.tables);
        rv.setLayoutManager(new GridLayoutManager(getActivity() , 2));

        //DUMMUY TABLES
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

        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrdersDatabase orderModel = Room.databaseBuilder(getActivity() , OrdersDatabase.class , "OrdersDB").build();

                //Create Order Object
                OrderEntry orderEntry = new OrderEntry();
                orderEntry.setCurrent(1);
                orderEntry.setBalance(0);
                orderEntry.setCust_num("9596");
                orderEntry.setDate_time("");
                Random random = new Random();
                orderEntry.setOrder_no(String.valueOf(Math.abs(random.nextInt())));
                orderEntry.setDiscount(0);
                orderEntry.setTable_no(1);
                orderEntry.setSynched(0);
                orderEntry.setTicket_cost(10);

                OrdersDatabase order = OrdersDatabase.getInstance(getActivity());
                order.ordersDao().insertOrder(orderEntry);

                Toast.makeText(getActivity() , "" +order.ordersDao().getAllOrders().length ,Toast.LENGTH_SHORT).show();
//
//                FirebaseDatabase.getInstance().getReference(getString(R.string.business_parent_node))
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(getString(R.string.Orders_node)).child(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
//                        .push().setValue(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
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

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView table_no;
        public TextView cost_on_table;
        public ViewHolder(View itemView) {
            super(itemView);
            table_no = itemView.findViewById(R.id.textView14);
        }
    }
}
