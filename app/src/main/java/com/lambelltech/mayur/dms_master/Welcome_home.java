package com.lambelltech.mayur.dms_master;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Welcome_home extends AppCompatActivity implements AdapterView.OnItemClickListener {
    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        grid = (GridView) findViewById(R.id.admin_grid_dashboard);
        grid.setAdapter(new gridAdapter(this) );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //mention intent here
    }
    public class DashboardItem {
        int dashboardIcon;
        String dashboardIconName;
        DashboardItem(int dashboardIcon,String dashboardIconName){
            this.dashboardIcon=dashboardIcon;
            this.dashboardIconName=dashboardIconName;
        }
    }
    public class gridAdapter extends BaseAdapter{
        ArrayList<DashboardItem> list;
        Context context;
        gridAdapter(Context context){
            this.context=context;
          list = new ArrayList<DashboardItem>();
            Resources res=context.getResources();
            String[] tempdashboardIconName= res.getStringArray(R.array.dashboardIconName);
            int[] dashboardIconId = {
                    R.drawable.event,
                    R.drawable.lisevent,
                    R.drawable.listser,
                    R.drawable.assignevent,
                    R.drawable.userwisevent,
                    R.drawable.aduser,
                    R.drawable.addvent
            };
            for (int i=0;i<7;i++){
                DashboardItem tempDashboardItem = new DashboardItem(dashboardIconId[i],tempdashboardIconName[i]);
                list.add(tempDashboardItem);
            }
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        //this one is used for the databases related activities
        public long getItemId(int i) {
            return i;
        }
        public class ViewHolder{
            ImageView myDashboardIcon;
            ViewHolder(View v){
                myDashboardIcon=(ImageView) v.findViewById(R.id.singleItem);
            }
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row=view;
            ViewHolder holder = null;
            if(row==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row= inflater.inflate(R.layout.single_item,viewGroup,false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            }else {
                holder= (ViewHolder) row.getTag();
            }
            DashboardItem temp =list.get(i);
            holder.myDashboardIcon.setImageResource(temp.dashboardIcon);
            return row;
        }
    }
}
