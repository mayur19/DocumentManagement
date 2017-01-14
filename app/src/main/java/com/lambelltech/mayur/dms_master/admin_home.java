package com.lambelltech.mayur.dms_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import fragments.Admin.ActivateUserFragment;
import fragments.Admin.admin_add_event;
import fragments.Admin.admin_add_user;
import fragments.Admin.admin_dashboard_home;
import fragments.Admin.admin_event_assign;
import fragments.Admin.admin_events;
import fragments.Admin.admin_list_events;
import fragments.Admin.admin_userwise_events;


public class admin_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String event_id;
    public static String admin_id;
    public static String user_id;
    public static ArrayList<String> temp = new ArrayList<String>();
    public static ArrayList<String> contextMenu;
    public static int count=0;
    public static int flag=0;
    public static String documentURL;
    public static int item;
    public static String Description;
    public static String Name;
    public static String time;
    public static String Flag="";
    public static String file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();
        admin_id = intent.getStringExtra("adminid");
        String fullname = intent.getStringExtra("fullname");
        //Toast.makeText(this, admin_id + " : " + fullname, Toast.LENGTH_SHORT).show();

        setAdmin_id(admin_id);
        contextMenu = new ArrayList<>();
        contextMenu.add("Details");
        contextMenu.add("Share");
        contextMenu.add("Delete");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_admin_home, new admin_dashboard_home()).addToBackStack(null).commit();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.admin_dashboard) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_dashboard_home()).commit();

            // Handle the camera action
        } else if (id == R.id.admin_events) {

            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_events()).addToBackStack(null).commit();

            // Handle the camera action
        } else if (id == R.id.admin_event_list) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_list_events()).addToBackStack(null).commit();
        } else if (id == R.id.admin_user_list) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new ActivateUserFragment()).addToBackStack(null).commit();
        } else if (id == R.id.admin_assign_event) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_event_assign()).addToBackStack(null).commit();
        } else if (id == R.id.admin_userwise_events) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_userwise_events()).addToBackStack(null).commit();
        } else if (id == R.id.admin_add_event) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_add_event()).addToBackStack(null).commit();
        }
        /* else if (id == R.id.admin_add_user) {
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_add_user()).addToBackStack(null).commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title){
        //YOUR_CUSTOM_ACTION_BAR_TITLE.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    public static String getEvent_id() {
        return event_id;
    }

    public static String getAdmin_id() {
        return admin_id;
    }

    public static void setEvent_id(String event_id) {
        admin_home.event_id = event_id;
    }

    public static void setAdmin_id(String admin_id) {
        admin_home.admin_id = admin_id;
    }

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        admin_home.user_id = user_id;
    }

    public static ArrayList<String> getTemp() {
        return temp;
    }

    public static void setTemp(ArrayList<String> temp) {
        admin_home.temp = temp;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        admin_home.count = count;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        admin_home.flag = flag;
    }

    public static String getDocumentURL() {
        return documentURL;
    }

    public static void setDocumentURL(String documentURL) {
        admin_home.documentURL = documentURL;
    }

    public static int getItem() {
        return item;
    }

    public static void setItem(int item) {
        admin_home.item = item;
    }

    public static ArrayList<String> getContextMenu() {
        return contextMenu;
    }

    public static void setContextMenu(ArrayList<String> contextMenu) {
        admin_home.contextMenu = contextMenu;
    }

    public static String getDescription() {
        return Description;
    }

    public static void setDescription(String description) {
        Description = description;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static void setFlag(String flag) {
        Flag = flag;
    }

    public static String getFile_id() {
        return file_id;
    }

    public static void setFile_id(String file_id) {
        admin_home.file_id = file_id;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        admin_home.time = time;
    }
}
