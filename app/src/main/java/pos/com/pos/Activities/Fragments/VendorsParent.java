package pos.com.pos.Activities.Fragments;

import android.content.Context;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");

        return root;
    }

    private void setupNavigation(View root){

//        AHBottomNavigation bottomNavigation = root.findViewById(R.id.bottom_navigation);
//
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Vendors", R.drawable.baseline_assignment_black_18dp, R.color.loginStatusBar);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Orders", R.drawable.baseline_local_shipping_black_18dp, R.color.loginStatusBar);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Explore", R.drawable.baseline_explore_black_18dp, R.color.loginStatusBar);
//
//        bottomNavigation.addItem(item1);
//        bottomNavigation.addItem(item2);
//        bottomNavigation.addItem(item3);

        //Additional properties\

        BottomNavigationView navigationView = root.findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.Vendors:
                        transaction.replace(R.id.fl , new VendorsOwn()).commit();
                        break;
                    case R.id.Orders:
                        transaction.replace(R.id.fl , new VendorOrders()).commit();
                        break;
                    case R.id.Explore:
                        transaction.replace(R.id.fl , new VendorsOwn()).commit();
                        break;
                }
                return true;
            }
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
