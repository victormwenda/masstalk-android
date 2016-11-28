package nerdygeek.browser;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Project - android-browser
 * Package - nerdygeek.browser
 * <p>
 * Victor Mwenda
 * +254(0)718034449
 * vmwenda.vm@gmail.com
 * <p>
 * Android App Development Laptop
 * Created by victor on 11/28/2016 at 11:57 AM.
 */
public class BrowserWebChromeClient extends WebChromeClient {

    private final MainActivity mainActivity;

    public BrowserWebChromeClient(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        getMainActivity().updateLoadingProgress(newProgress);
    }
}
