package fragments.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.admin_home;
import com.lambelltech.mayur.dms_master.admin_login_activity;

/**
 * Created by aishwarya on 07-Jan-17.
 */
public class DetailsFragment extends Fragment {

    TextView nameTextView,descriptionTextView,createdTimeTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_details, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Details");

        nameTextView = (TextView)mainView.findViewById(R.id.xFilenameTextView);
        descriptionTextView = (TextView)mainView.findViewById(R.id.xDescriptionTextView);
        createdTimeTv = (TextView)mainView.findViewById(R.id.xEventTimeTextView);
        nameTextView.setText(admin_home.Name);
        descriptionTextView.setText(admin_home.Description);
        createdTimeTv.setText(admin_home.time);

        //setHasOptionsMenu(true);
        return mainView;
    }

}
