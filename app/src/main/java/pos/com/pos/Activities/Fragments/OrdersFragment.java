package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        //IMP SAMPLE DATA
        final int[] table_no = new int[1];
        final float order_price= 100;
        final String order_type ="Table";

        final View bottomSheet = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);

        TextView orders = root.findViewById(R.id.orders);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");
        orders.setTypeface(custom_font);




        //FOR ORDERS
        RecyclerView rv_orders = root.findViewById(R.id.order_view_rv);
        rv_orders.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.order_card_holder , container , false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return orderModels.size();
            }
        };

        rv_orders.setAdapter(adapter);
        //Get Orders

        FirebaseDatabase.getInstance().getReference(getString(R.string.business_parent_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.Orders_node))
                .child(new SimpleDateFormat("dd-MMM-yyyy").format(new Date())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderModels.clear();
                Iterable<DataSnapshot> orderModels_iteratable = dataSnapshot.getChildren();
                for (DataSnapshot d : orderModels_iteratable){
                    orderModels.add(d.getValue(OrderModel.class));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //FOR TABLES
        RecyclerView rv = root.findViewById(R.id.tables);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.HORIZONTAL));

        //DUMMUY TABLES
        RecyclerView.Adapter<ViewHolder> viewHolderAdapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.table_layout,container ,false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                holder.no.setText(""+(position+1));
                holder.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        table_no[0] = (position+1);
                    }
                });
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

                OrderModel orderModel = new OrderModel(order_type,table_no[0] , order_price);

                FirebaseDatabase.getInstance().getReference(getString(R.string.business_parent_node))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.Orders_node)).child(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
                        .push().setValue(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
                    Toast.makeText(getActivity(), "Collapsed by dragging", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if (slideOffset == 0.0) {
                    Toast.makeText(getActivity(), "Dragged down", Toast.LENGTH_SHORT).show();
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
        public TextView no;
        public ViewHolder(View itemView) {
            super(itemView);

            no = itemView.findViewById(R.id.table_no);
        }
    }
}
