package pos.com.pos.Activities.Activities;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Fragments.DashBoard;
import pos.com.pos.Activities.Fragments.MenuFragment;
import pos.com.pos.Activities.Fragments.OrdersFragment;
import pos.com.pos.Activities.Fragments.VendorOrders;
import pos.com.pos.Activities.Fragments.VendorsExplore;
import pos.com.pos.Activities.Fragments.VendorsOwn;
import pos.com.pos.Activities.Fragments.VendorsParent;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.R;

public class HolderActivity extends AppCompatActivity
        implements

        NavigationView.OnNavigationItemSelectedListener,
        DashBoard.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener,
        OrdersFragment.OnFragmentInteractionListener, VendorsParent.OnFragmentInteractionListener,
        VendorsExplore.OnFragmentInteractionListener ,VendorsOwn.OnFragmentInteractionListener , VendorOrders.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

       //Check if the guy completed his setup via User Config

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new DashBoard()).commit();

        FirebaseAssistant.initFire(HolderActivity.this);

        //call sync function
        sync_orders();

        //Init bottom nav bar
        BottomSheetBehavior<View> bottomSheetBehavior = toggleNavigation();

        //Set fonts
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this , "fonts/Product Sans Bold.ttf" , true);

        //setuo bottom nav bar
        RecyclerView.Adapter<ViewHolder> adapter = initializeNavbar((TextView) findViewById(R.id.textView)
                ,bottomSheetBehavior, calligrapher  );

        


    }


    public void sync_orders(){

        final FirebaseAssistant assistant = new FirebaseAssistant();


        //Start Syncing After every 1 minute
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                assistant.syncOrders();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask , 60000, 60000);

    }


    //navbar method
    RecyclerView.Adapter<ViewHolder> initializeNavbar(final TextView navbarTitle
            , final BottomSheetBehavior bottombar
            , final Calligrapher calligrapher){


        final String options[] ={
                "Home",
                "Orders",
                "Menu",
                "Vendors",
                "Records"
        };

        //REcyler view
        final RecyclerView nav_bar = findViewById(R.id.nav_options);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        nav_bar.setLayoutManager(manager);



        //Set navbar
        RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_options , parent , false));
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


                //set navbar options
                holder.option_item.setText(options[holder.getAdapterPosition()]);


                //Update navbar head on select

                holder.option_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        //Update view
                        navbarTitle.setText(options[holder.getAdapterPosition()]);
                        bottombar.setState(BottomSheetBehavior.STATE_COLLAPSED);


                        //Use postion to determine fragment
                        switch (holder.getAdapterPosition()){
                            case 0:
                                transaction.replace(R.id.fragment_container , new DashBoard()).commit();
                                break;
                            case 1:

                                transaction.replace(R.id.fragment_container , new OrdersFragment()).commit();

                                break;

                            case 2:

                                transaction.replace(R.id.fragment_container , new MenuFragment()).commit();
                                break;

                            case 3:

                                transaction.replace(R.id.fragment_container , new VendorsParent()).commit();
                                break;

                            case 4:

                                transaction.replace(R.id.fragment_container , new DashBoard()).commit();
                                break;

                        }




                    }
                });



                //Set navbar fonts

                //TODO fix bug where last item is not custom fonted
                calligrapher.setFont(findViewById(R.id.bottom_navigation),"fonts/Product Sans Bold.ttf");



            }

            @Override
            public int getItemCount() {
                return 5;
            }
        };

        //Set adapter
        nav_bar.setAdapter(adapter);


        return adapter;
    }

    //navigation sheet enables
    BottomSheetBehavior<View> toggleNavigation(){


        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_navigation));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {


                //change color by calling specified method
                switch (i){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        return bottomSheetBehavior;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView option_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            option_item = itemView.findViewById(R.id.option);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.holder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_camera) {

            transaction.replace(R.id.fragment_container, new MenuFragment()).commit();

        } else if (id == R.id.nav_gallery) {

            transaction.replace(R.id.fragment_container, new OrdersFragment()).commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
