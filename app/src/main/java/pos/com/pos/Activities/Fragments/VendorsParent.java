package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Models.Vendor;
import pos.com.pos.Activities.Models.Vendors_orders_model;
import pos.com.pos.R;

public class VendorsParent extends Fragment  implements VendorOrders.OnFragmentInteractionListener,
VendorsOwn.OnFragmentInteractionListener ,VendorsExplore.OnFragmentInteractionListener{

    private OnFragmentInteractionListener mListener;

    public VendorsParent() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_vendors_parent, container, false);

        FragmentTransaction transaction =getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl , new VendorsOwn()).commit();


        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");

        setupNavigation(root , calligrapher);

        return root;
    }

    private void setupNavigation(View root , Calligrapher calligrapher){

        AHBottomNavigation bottomNavigation = root.findViewById(R.id.botton_nav_bar);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Vendors", R.drawable.baseline_assignment_black_18dp, R.color.loginStatusBar);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Orders", R.drawable.baseline_local_shipping_black_18dp, R.color.loginStatusBar);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Explore", R.drawable.baseline_explore_black_18dp, R.color.loginStatusBar);

        calligrapher.setFont(bottomNavigation , "fonts/Product Sans Bold.ttf");

        bottomNavigation.setAccentColor(Color.parseColor("#000000"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FF424242"));

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);


        //Set up fab with the bottom navigation bar

        final Vendors_orders_model order = new Vendors_orders_model();

        FloatingActionButton fab = root.findViewById(R.id.fab);
        bottomNavigation.manageFloatingActionButtonBehavior(fab);

        ArrayList<String> orders_models = new ArrayList<>();
        orders_models.add("Hello");
        orders_models.add("item23");

        //Make object to push to firebase
        order.setItems(orders_models);
        order.setOrdered_at("Today");
        order.setPriority(1);
        order.setUid_vendor("aaaaaa");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Businesses")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Vendors_info")
                        .child("Vendor_orders")
                        .child("24-10-2018").setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity() , "Pushed" , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentTransaction transaction =getFragmentManager().beginTransaction();

                switch (position){
                    case 0:
                        transaction.replace(R.id.fl , new VendorsOwn()).commit();
                        break;
                    case 1:
                        transaction.replace(R.id.fl , new VendorOrders()).commit();
                        break;
                    case 2:
                        transaction.replace(R.id.fl , new VendorsExplore()).commit();
                        break;
                }
                return true;            }
        });


    }

    private ArrayList<Vendor> getVendors(){

        final ArrayList<Vendor> vendors = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Businesses")
                .child(FirebaseAuth.getInstance().getUid()).child("Vendor_info")
                .child("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();for (DataSnapshot d:
                     snapshots) {
                    vendors.add(d.getValue(Vendor.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vendors ;
    }

    private void getVendors(final RecyclerView.Adapter adapter){

        final ArrayList<Vendor> vendors = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Businesses")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Vendor_info")
                .child("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vendors.clear();

                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();for (DataSnapshot d:
                        snapshots) {
                    vendors.add(d.getValue(Vendor.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
