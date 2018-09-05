package pos.com.pos.Activities.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Timer;
import java.util.TimerTask;

import pos.com.pos.Activities.Fragments.DashBoard;
import pos.com.pos.Activities.Fragments.DashBoardContent;
import pos.com.pos.Activities.Fragments.Dashboard_Notifications;
import pos.com.pos.Activities.Fragments.MenuFragment;
import pos.com.pos.Activities.Fragments.OrdersFragment;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.R;

public class HolderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashBoard.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener, OrdersFragment.OnFragmentInteractionListener ,Dashboard_Notifications.OnFragmentInteractionListener,
        DashBoardContent.OnFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        //Check if the guy completed his setup via User Config

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new DashBoard()).commit();

        FirebaseAssistant.initFire(HolderActivity.this);

        //call sync function
        sync_orders();

        //setuo bottom nav bar
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem dashboard = new AHBottomNavigationItem("Dashboard", R.drawable.side_nav_bar);
        AHBottomNavigationItem dashboard1 = new AHBottomNavigationItem("Dashboard", R.drawable.side_nav_bar);
        AHBottomNavigationItem dashboard2 = new AHBottomNavigationItem("Dashboard", R.drawable.side_nav_bar);



        bottomNavigation.addItem(dashboard);
        bottomNavigation.addItem(dashboard1);
        bottomNavigation.addItem(dashboard2);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

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
}
