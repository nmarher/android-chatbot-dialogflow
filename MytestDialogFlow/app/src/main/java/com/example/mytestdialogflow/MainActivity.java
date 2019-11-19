package com.example.mytestdialogflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mytestdialogflow.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    WebView miVisorWeb;
    String url = "https://console.dialogflow.com/api-client/demo/embedded/5c9c3e0e-7e43-4518-8f19-977a3c56b31a";
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
    private ActivityMainBinding mBinding;
    private PermissionRequest myRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        setWebView();
    }


        private void setWebView() {
            mBinding.visorWeb.getSettings().setJavaScriptEnabled(true);
            mBinding.visorWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mBinding.visorWeb.setWebViewClient(new WebViewClient());
            mBinding.visorWeb.getSettings().setSaveFormData(true);
            mBinding.visorWeb.getSettings().setSupportZoom(false);
            mBinding.visorWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            mBinding.visorWeb.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    myRequest = request;

                    for (String permission : request.getResources()) {
                        switch (permission) {
                            case "android.webkit.resource.AUDIO_CAPTURE": {
                                askForPermission(request.getOrigin().toString(), Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                                break;
                            }
                        }
                    }
                }
            });

            mBinding.visorWeb.loadUrl(url);
        }

        @Override
        public void onBackPressed() {
            if (mBinding.visorWeb.canGoBack()) {
                mBinding.visorWeb.goBack();
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                    Log.d("WebView", "PERMISSION FOR AUDIO");
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        myRequest.grant(myRequest.getResources());
                        mBinding.visorWeb.loadUrl(url);

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
        public void askForPermission(String origin, String permission, int requestCode) {
            Log.d("WebView", "inside askForPermission for" + origin + "with" + permission);

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        permission)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{permission},
                            requestCode);
                }
            } else {
                myRequest.grant(myRequest.getResources());
            }
        }

    //Impedir que el botón Atrás cierre la aplicación
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView miVisorWeb;
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (miVisorWeb.canGoBack()) {
                        miVisorWeb.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
