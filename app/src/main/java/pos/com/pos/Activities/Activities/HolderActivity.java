package pos.com.pos.Activities.Activities;

import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Timer;
import java.util.TimerTask;

import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Fragments.DashBoard;
import pos.com.pos.Activities.Fragments.MenuFragment;
import pos.com.pos.Activities.Fragments.OrdersFragment;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class HolderActivity extends AppCompatActivity
        implements

        NavigationView.OnNavigationItemSelectedListener,
        DashBoard.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener,
        OrdersFragment.OnFragmentInteractionListener {


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

        //setuo bottom nav bar

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
