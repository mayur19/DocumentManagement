package fragments.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lambelltech.mayur.dms_master.MainActivity;
import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.user_home;
/**
 * Created by aishwarya on 08-Jan-17.
 */
public class OnSharedNextFragment extends Fragment {

    ListView eventListView;
    ArrayAdapter<String> adapter;
    FragmentManager fm;
    String[] type = {"Events","Documents"};
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_user_events, container, false);
        ((user_home) getActivity()).setActionBarTitle("Shared Display");

        eventListView = (ListView)mainView.findViewById(R.id.xEventListView);

        fm = getActivity().getSupportFragmentManager();
        progressBar=(ProgressBar)mainView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, type);

        eventListView.setAdapter(adapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                    fm.beginTransaction().replace(R.id.frame_user_home, new SharedEventsDisplay()).addToBackStack(null).commit();
                if (position==1)
                    fm.beginTransaction().replace(R.id.frame_user_home, new SharedDocumentDisplay()).addToBackStack(null).commit();
            }
        });

        //setHasOptionsMenu(true);
        return mainView;
    }
}
