package nerdygeek.browser;

import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Project - android-browser
 * Package - nerdygeek.browser
 * <p>
 * Victor Mwenda
 * +254(0)718034449
 * vmwenda.vm@gmail.com
 * <p>
 * Android App Development Laptop
 * Created by victor on 11/28/2016 at 11:50 AM.
 */
public class BrowserWebViewClient extends WebViewClient {

    private final MainActivity mainActivity;

    public BrowserWebViewClient(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        getMainActivity().onReceivedError(getMainActivity().getWebPage(), request, error);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shouldOverrideUrlLoading(getMainActivity().getWebPage(), request.getUrl().toString());
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        getMainActivity().setTitle(getMainActivity().getWebPage().getTitle());
        getMainActivity().getWebPage().loadUrl(url);
        getMainActivity().interceptRequest(view);
        return super.shouldOverrideUrlLoading(view, url);
    }
}
