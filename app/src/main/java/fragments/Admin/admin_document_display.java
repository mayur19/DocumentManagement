package fragments.Admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.lambelltech.mayur.dms_master.R;
import com.lambelltech.mayur.dms_master.admin_home;

import java.io.InputStream;

/**
 * Created by mayur on 26-12-2016.
 */

public class admin_document_display extends Fragment {

    ImageView image;
    public static String URL="";
    WebView document;
    public static ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_document_display, container, false);
        ((admin_home) getActivity()).setActionBarTitle("Document Display");

        image = (ImageView)mainView.findViewById(R.id.xImageView);
        document = (WebView)mainView.findViewById(R.id.xWebView);

        URL = admin_home.getDocumentURL();

        String tempurl[] = URL.split("\\.");
        int i=0;
        for (i=0;i<tempurl.length;++i);

        //Toast.makeText(getActivity(), tempurl[--i], Toast.LENGTH_SHORT).show();
        String str = tempurl[--i];
        if (str.equalsIgnoreCase("jpg") ||
                str.equalsIgnoreCase("jpeg") ||
                str.equalsIgnoreCase("png")){

            //Toast.makeText(getActivity(), "image", Toast.LENGTH_SHORT).show();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading Image");
            new DownloadImageTask(image).execute(URL);
        }else {
            Toast.makeText(getActivity(), "document", Toast.LENGTH_SHORT).show();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading document");
            document.setWebViewClient(new AppWebViewClients());
            document.getSettings().setJavaScriptEnabled(true);
            document.getSettings().setUseWideViewPort(true);
            document.loadUrl("http://docs.google.com/gview?embedded=true&url=" + URL);
        }

        setHasOptionsMenu(true);
        return mainView;
    }


}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        admin_document_display.pd.show();
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        admin_document_display.pd.dismiss();
        bmImage.setImageBitmap(result);
    }
}

class AppWebViewClients extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);

        if (admin_document_display.pd.isShowing()) {
            admin_document_display.pd.dismiss();
            admin_document_display.pd = null;
        }

    }
}
