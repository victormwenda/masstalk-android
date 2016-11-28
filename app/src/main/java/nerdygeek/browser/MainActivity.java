package nerdygeek.browser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import static android.view.View.GONE;

/**
 * Project - Browser
 * Package - nerdygeek.browser
 * <p>
 * Victor Mwenda
 * +254(0)718034449
 * vmwenda.vm@gmail.com
 * <p>
 * Android App Development Laptop
 * Created by victor on 11/28/2016 at 11:26 AM.
 */

public class MainActivity extends AppCompatActivity {

    private ContentLoadingProgressBar mClPbLoading;

    private WebView mWvPage;

    private RelativeLayout mRlBottomBar;
    private AppCompatImageView mIvBack;
    private AppCompatImageView mIvForward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initWebView();
    }

    @Override
    public void onBackPressed() {
        if (canGoBack()) {
            goBack();
        } else
            super.onBackPressed();
    }

    private void initViews() {
        mClPbLoading = (ContentLoadingProgressBar) findViewById(R.id.activity_main_clpb_progress);
        mClPbLoading.setMax(100);

        mWvPage = (WebView) findViewById(R.id.activity_main_wv_page);

        mRlBottomBar = (RelativeLayout) findViewById(R.id.activity_main_rl_bottom_bar);

        mIvBack = (AppCompatImageView) findViewById(R.id.activity_main_aiv_back);
        mIvForward = (AppCompatImageView) findViewById(R.id.activity_main_aiv_forward);

        mIvForward.setOnClickListener(this::onClickNavigation);
        mIvBack.setOnClickListener(this::onClickNavigation);
    }

    public WebView getWebPage() {
        return mWvPage;
    }

    public ContentLoadingProgressBar getProgress() {
        return mClPbLoading;
    }

    private void onClickNavigation(View view) {
        if (view == mIvBack) {
            goBack();
        }
        if (view == mIvForward) {
            goForward();
        }
    }

    private boolean canGoBack() {
        return getWebPage().canGoBack();
    }

    private boolean canGoForward() {
        return getWebPage().canGoForward();
    }

    private void goBack() {
        if (canGoBack()) {
            getWebPage().goBack();
            interceptRequest(getWebPage());
        }
    }

    private void goForward() {
        if (canGoForward()) {
            getWebPage().goForward();
            interceptRequest(getWebPage());
        }
    }

    private void initWebView() {
        getWebPage().getSettings().setJavaScriptEnabled(true);
        getWebPage().getSettings().setSupportZoom(true);
        getWebPage().getSettings().setGeolocationEnabled(true);
        getWebPage().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getWebPage().getSettings().setAllowFileAccess(true);
        getWebPage().getSettings().setAllowContentAccess(true);
        getWebPage().getSettings().setUserAgentString("chrome-mobile");
        getWebPage().getSettings().setAppCacheEnabled(true);
        getWebPage().getSettings().setDomStorageEnabled(true);
        getWebPage().setWebViewClient(new BrowserWebViewClient(this));
        getWebPage().setWebChromeClient(new BrowserWebChromeClient(this));

        interceptRequest(getWebPage());
        updateLoadingProgress(0);
        getWebPage().loadUrl("http://sema.comhpoafrica.com");
    }

    public void hideBottomBar() {
        mRlBottomBar.setVisibility(GONE);
    }

    public void showBottomBar() {
        mRlBottomBar.setVisibility(View.VISIBLE);
    }

    public void showBackArrow() {
        mIvBack.setVisibility(canGoBack() ? View.VISIBLE : View.GONE);
    }

    public void showForwardArrow() {
        mIvForward.setVisibility(canGoForward() ? View.VISIBLE : View.GONE);
    }

    /**
     * Intercept the http request
     *
     * @param view the web page
     */
    public void interceptRequest(WebView view) {
        if (!view.canGoBack() && !view.canGoForward()) {
            hideBottomBar();
        } else {
            showBottomBar();
            showForwardArrow();
            showBackArrow();
        }
    }

    /**
     * Called to update the loading progress of the web page
     *
     * @param newProgress the new progress
     */
    public void updateLoadingProgress(int newProgress) {
        if (newProgress > 0 && newProgress < 99) {
            getProgress().setVisibility(View.VISIBLE);
            getProgress().setProgress(newProgress);
        }

        if (newProgress == 0 || newProgress == 100) {
            getProgress().setVisibility(View.GONE);
        }
    }

    /**
     * Called when an error occurs
     *
     * @param view    the web page
     * @param request the request
     * @param error   the error
     */
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showErrorDialog("Error", error.getDescription());
        } else {
            showErrorDialog("Error", "Cannot load the web page. Ensure you have a working internet connection");
        }
    }

    /**
     * Show a dialog
     *
     * @param title   title of the dialog
     * @param message message of the dialog
     */
    public void showErrorDialog(String title, CharSequence message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("Check Internet", ((dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS))));
        alert.setNegativeButton("Refresh", (dialogInterface, i) -> getWebPage().reload());
        alert.show();
    }

}
