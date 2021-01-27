package com.frarisstudio.lavapiatti;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.frarisstudio.lavapiatti.system.User;
import com.frarisstudio.lavapiatti.system.drawmenu.FamilyF;
import com.frarisstudio.lavapiatti.system.drawmenu.HomeF;
import com.frarisstudio.lavapiatti.system.drawmenu.ProfileF;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    TextView headTitle, subTitle;

    String uid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //RECUPERO L'UTENTE
        recoverUser();

        //NASCONDO ACTIONBAR
        getSupportActionBar().hide();

        recoverUID();
        //CREO FRAGMENT DI DEFAULT
        Fragment f = new HomeF();
        //AGGIUNGO FRAGMENT DI DEFAULT
        addFragment(f);
        //SETTAGGIO TITOLI DEL MENU
        //setTitle();

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // INIZIALIZZO IL MENU
        setupDrawerContent(nvDrawer);


        // This will display an Up icon (<-), we will replace it with hamburger later
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void recoverUID() {
        Bundle extrasUid = getIntent().getExtras();
        uid = extrasUid.getString("uid");
    }

    private void recoverUser() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        uid = firebaseUser.getUid();
    }

    private void setTitle() {
        headTitle = findViewById(R.id.menu_header_title);
        headTitle.setText("PROVA");
        subTitle = findViewById(R.id.menu_subheader_title);
        subTitle.setText("PROVA");
    }

    public void addFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flContent, f);
        transaction.commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_second_fragment:
                FamilyF familyF = new FamilyF();
                addFragment(familyF);
                break;
            case R.id.nav_third_fragment:
                ProfileF profileF = new ProfileF();
                addFragment(profileF);
                break;
            case R.id.nav_logout:
                doLogout();
                break;
            case R.id.nav_first_fragment:
                HomeF homeF = new HomeF();
                addFragment(homeF);
                break;
            default:
                HomeF homeF2 = new HomeF();
                addFragment(homeF2);

        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private void doLogout() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ // use android.R.id
            mDrawer.openDrawer(Gravity.LEFT);
        }
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

}