package fragments.Admin;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambelltech.mayur.dms_master.LocalDbHelper;
import com.lambelltech.mayur.dms_master.MainActivity;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.SplashEntry;
import com.lambelltech.mayur.dms_master.admin_home;
import com.lambelltech.mayur.dms_master.admin_login_activity;

import java.util.ArrayList;

/**
 * Created by mayur on 13-12-2016.
 */

public class admin_dashboard_home extends Fragment implements AdapterView.OnItemClickListener {
    GridView grid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Dashboard");
        setHasOptionsMenu(true);
        grid = (GridView) mainView.findViewById(R.id.admin_grid_dashboard);
        grid.setAdapter(new admin_dashboard_home.gridAdapter(mainView.getContext()) );
        grid.setOnItemClickListener(this);
        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_admin_dashboard, menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

        switch (item.getItemId()) {

            /** EDIT **/
            case R.id.logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LocalDbHelper mydb = new LocalDbHelper(getActivity());
                                        mydb.executeQuery("root", "root", 0);
                                        Intent intent = new Intent(getActivity(), SplashEntry.class);
                                        getActivity().startActivity(intent);

                                    }
                                })
                .create().show();
                return true;

            case R.id.search:

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //mention intent here
        FragmentManager fm = getActivity().getSupportFragmentManager();

        if(l== grid.getItemIdAtPosition(0)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_events()).addToBackStack(null).commit();
        }else  if(l== grid.getItemIdAtPosition(1)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_list_events()).addToBackStack(null).commit();
        }else  if(l== grid.getItemIdAtPosition(2)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new ActivateUserFragment()).addToBackStack(null).commit();
        }else  if(l== grid.getItemIdAtPosition(3)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_event_assign()).addToBackStack(null).commit();
        }else  if(l== grid.getItemIdAtPosition(4)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_userwise_events()).addToBackStack(null).commit();
        }else  if(l== grid.getItemIdAtPosition(5)){
            fm.beginTransaction().replace(R.id.frame_admin_home, new admin_add_event()).addToBackStack(null).commit();
        }
    }
    class DashboardItem {
        int dashboardIcon;
        String dashboardIconName;
        DashboardItem(int dashboardIcon,String dashboardIconName){
            this.dashboardIcon=dashboardIcon;
            this.dashboardIconName=dashboardIconName;
        }
    }
    class gridAdapter extends BaseAdapter {
        ArrayList<admin_dashboard_home.DashboardItem> list;
        Context context;

        gridAdapter(Context context) {
            this.context = context;
            list = new ArrayList<admin_dashboard_home.DashboardItem>();
            Resources res = context.getResources();
            String[] tempdashboardIconName = res.getStringArray(R.array.dashboardIconName);
            int[] dashboardIconId = {
                    R.drawable.event,
                    R.drawable.lisevent,
                    R.drawable.listser,
                    R.drawable.assignevent,
                    R.drawable.userwisevent,
                    R.drawable.addvent
            };
            for (int i = 0; i < 6; i++) {
                admin_dashboard_home.DashboardItem tempDashboardItem = new admin_dashboard_home.DashboardItem(dashboardIconId[i], tempdashboardIconName[i]);
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

        class ViewHolder {
            ImageView myDashboardIcon;
            TextView myDashboardIconName;

            ViewHolder(View v) {
                myDashboardIcon = (ImageView) v.findViewById(R.id.singleItem);
                myDashboardIconName=(TextView)v.findViewById(R.id.singleItemName);
            }
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            admin_dashboard_home.gridAdapter.ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_item, viewGroup, false);
                holder = new admin_dashboard_home.gridAdapter.ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (admin_dashboard_home.gridAdapter.ViewHolder) row.getTag();
            }
           admin_dashboard_home.DashboardItem temp = list.get(i);
            holder.myDashboardIcon.setImageResource(temp.dashboardIcon);
            holder.myDashboardIconName.setText(temp.dashboardIconName);
            return row;
        }
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button

                    return true;

                }

                return false;
            }
        });
    }

}
