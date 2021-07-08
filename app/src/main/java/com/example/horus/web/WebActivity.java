package com.example.horus.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.ui.BaseActivity;
import com.example.horus.utils.DialogUtils;
import com.example.horus.utils.StatusBarUtil;
import com.example.horus.widget.ToolbarView;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class WebActivity extends BaseActivity {

    private static String TAG = WebActivity.class.getSimpleName() + "_TAG";
    private static final int REQUEST_SHEAR_CODE = 111;

    private WebView mWebView;
    @BindView(R.id.container)
    ViewGroup mContainer;
    @BindView(R.id.toolbar)
    ToolbarView mToolbarView;

    private ValueCallback<Uri> mUploadCallbackBelowL;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    public static final String BUNDLE_ORDER_ID = "bundle_order_id";
    public static final String BUNDLE_PRICE_TOTAL = "bundle_price_total";
    public static final String BUNDLE_RECHARGE_RESULT = "bundle_recharge_result";

    public static final String BUNDLE_URL = "bundle_url";
    public static final String BUNDLE_TITLE = "bundle_title";
    public static final String BUNDLE_HAS_NAVIAGION = "bundle_has_naviagion";

    public static void start(Activity context, String orderID, String total, int requestCode) {

        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(BUNDLE_ORDER_ID, orderID);
        starter.putExtra(BUNDLE_PRICE_TOTAL, total);
        context.startActivityForResult(starter, requestCode);
    }


    public static void start(Context context, String url, String title) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(BUNDLE_URL, url);
        starter.putExtra(BUNDLE_TITLE, title);
        context.startActivity(starter);
    }

    public static void startNoNaviagion(Context context, String url, String title) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(BUNDLE_URL, url);
        starter.putExtra(BUNDLE_TITLE, title);
        starter.putExtra(BUNDLE_HAS_NAVIAGION, false);
        context.startActivity(starter);
    }


    private void cancelSelectPic(){
        // 加载结束js 方法 get_android_base
//        Uri uri = Uri.fromFile(new File(url));
        if (mUploadCallbackBelowL != null ){
            mUploadCallbackBelowL.onReceiveValue(null);
        }else if (mUploadCallbackAboveL != null){
//            Uri[] results = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(null);
        }

//        if (mWebView == null)return;
//        mWebView.evaluateJavascript("javascript:resetUploader()", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//                //此处为 js 返回的结果
//                LogUtil.d(TAG,"resetUploader:" + value);
//            }
//        });
    }

    public class JsInteration{

        @JavascriptInterface
        public void share(String shareTitle,String shareContent,String shareUrl,String shareIcon){
//            public ShareInfo(String popTitle, String shareTitle, String shareContent, String shareUrl,String shareIcon,
//            int  mediaType) {
//                this.popTitle = popTitle;
//                this.shareTitle = shareTitle;
//                this.shareContent = shareContent;
//                this.shareUrl = shareUrl;
//                this.shareIcon = shareIcon;
//                this.mediaType=mediaType;
//            }
//            LogUtil.d(TAG,"shareTitle:" + shareTitle + "shareContent:" + shareContent + "shareUrl:" + shareUrl + " shareIcon:" + shareIcon );
//            ShareInfo shareInfo = new ShareInfo(MyApp.getInstance().getString(R.string.shared_to_title),shareTitle,shareContent,shareUrl, shareIcon,ShareInfo.SHARE_MEDIA_TYPE_ACTIVITY);
//            ShareActivity.start(WebActivity.this,REQUEST_SHEAR_CODE,shareInfo);
        }

        @JavascriptInterface
        public void back(){
            finish();

        }

        @JavascriptInterface
        public void logcat(String message){
            LogUtil.d(TAG,message);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // web view 有可能不存在
        try {
            setContentView(R.layout.activity_web);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("webview")) {
                // If the system failed to inflate this view because of the WebView (which could
                // be one of several types of exceptions), it likely means that the system WebView
                // is either not present (unlikely) OR in the process of being updated (also unlikely).
                // It's unlikely but we have been receiving a lot of crashes.
                // In this case, show the user a message and finish the activity
                ToastUtil.showShortMessage("please install System WebView");
            }
        }
        ButterKnife.bind(this);
        initView();
        initEvent();
        postData();
    }


    private void initView() {
        mToolbarView.init(this, getIntent().getStringExtra(BUNDLE_TITLE), true, true);
        boolean isHasNavigation = getIntent().getBooleanExtra(BUNDLE_HAS_NAVIAGION,true);
        if (isHasNavigation){
            mToolbarView.setVisibility(View.VISIBLE);
        }else {
            mToolbarView.setVisibility(View.GONE);
            StatusBarUtil.setColorRes(this,R.color.spring_festival_bar_color);
//            ImmersionBar.with(this).statusBarColorTransform(R.color.spring_festival_bar_color).statusBarDarkFont(false);
        }
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mContainer.addView(mWebView);
        initJS();
        initWebView();
    }

    /**
     * 与h5通信
     * 回调
     */
    private void initJS() {
    }

    /**
     * 初始化
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBlockNetworkImage(false);
        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        // 是否支持viewport属性，默认值 false
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);


        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
//        webView.getSettings().setUserAgentString(MyApplication.getUserAgent());
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mWebView.setWebViewClient(new MineWebViewClient());
        mWebView.setWebChromeClient(new MineWebChromeClient());

        mWebView.loadUrl(getIntent().getStringExtra(BUNDLE_URL));
        mWebView.addJavascriptInterface(new JsInteration(),"android");
        /**
         *  //调用原生的方法，android为约定的别名；back()为原生的方法
         *  h5调用
         *  var result=window.android.back();
         */

    }

    private void initEvent() {
//        mWebView.registerHandler("submitFromWeb", (data, function) -> {
//            Log.e("data",data );

//            Intent intent=new Intent();
//            intent.putExtra(BUNDLE_RECHARGE_RESULT,data);
//            setResult(RESULT_OK,intent);
//            finish();
//        });
    }

    private void postData() {

//        String request=getIntent().getStringExtra(BUNDLE_PRICE_TOTAL)+","+
//                getIntent().getStringExtra(BUNDLE_ORDER_ID);
//        String request="10"+","+"USD";
//        mWebView.callHandler("functionInJs", request, data -> {
//
//        });
    }

        //https://blog.csdn.net/villa_mou/article/details/78256748?locationNum=3&fps=1
    private class MineWebViewClient extends WebViewClient {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                    case SslError.SSL_DATE_INVALID:
                        message = "The date of the certificate is invalid";
                        break;
                    case SslError.SSL_INVALID:
                    default:
                        message = "A generic error occurred";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);

                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            LogUtil.d(TAG, " MineWebViewClient 触发  load url   " + url);

            if (url.startsWith("http") || url.startsWith("https")) {
                view.loadUrl(url);
            } else {
                try {
                    final Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                }
            }
//
            return false;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtil.d(TAG, " MineWebViewClient onPageStarted:" + url);
        }


        /**
         * 页面加载完成之后加载图片
         *
         * @link initWebView
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.d(TAG, " MineWebViewClient onPageFinished:" + url);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest
                request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            LogUtil.d(TAG, " MineWebViewClient onReceivedError");
        }

    }

    private void checkPremission(){
//        PermissionTool permissionTool = new PermissionTool(this);
//        permissionTool.putPermissions(PermissionTool.albumWithCamera());
//        permissionTool.check(new PermissionTool.OnPermissionListener() {
//            @Override
//            public void onPermissionGranted(boolean granted) {
//                if (granted) {
//                    PictureSelectorManager.selectPictureBecomeChatter(WebActivity.this,5,4);
//                }else {
//                    cancelSelectPic();
//                }
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        // 图片选择结果
//        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
//            List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
//            if (mediaList != null && mediaList.size() == 1) {
//
//                LocalMedia media = mediaList.get(0);
//                PictureSelectorManager.setLocalMediaForAndroidQ(media);
//                String url = media.getPath();
//                // 图片是先裁剪再压缩
//                if (media.isCut()) {
//                    url = media.getCutPath();
//                } else if (media.isCompressed()) {
//                    url = media.getCompressPath();
//                }
//                Uri uri = Uri.fromFile(new File(url));
//                if (mUploadCallbackBelowL != null && uri != null){
//                    mUploadCallbackBelowL.onReceiveValue(uri);
//                }else if (mUploadCallbackAboveL != null && uri != null){
//                    Uri[] results = new Uri[]{uri};
//                    mUploadCallbackAboveL.onReceiveValue(results);
//                }
//
//            }
//        }else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode != RESULT_OK){
//            cancelSelectPic();
//        }

    }


    private class MineWebChromeClient extends WebChromeClient {


        public boolean onConsoleMessage(ConsoleMessage cm) {
            LogUtil.e(TAG, cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId() );
            return true;
        }

        /**
         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
         */
        private void openFileChooser(ValueCallback<Uri> uploadMsg) {
            LogUtil.d(TAG,"MineWebChromeClient" + "运行方法 openFileChooser-1");
            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
            mUploadCallbackBelowL = uploadMsg;
           checkPremission();
        }

        /**
         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            LogUtil.d(TAG,"MineWebChromeClient"+ "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
            // 这里我们就不区分input的参数了，直接用拍照
            openFileChooser(uploadMsg);
        }

        /**
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            LogUtil.d(TAG,"MineWebChromeClient"+"运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
            // 这里我们就不区分input的参数了，直接用拍照
            openFileChooser(uploadMsg);
        }

        /**
         * API >= 21(Android 5.0.1)回调此方法
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            LogUtil.d(TAG,"MineWebChromeClient"+"运行方法 onShowFileChooser");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
//            mFilePathCallback = valueCallback;
            mUploadCallbackAboveL = valueCallback;
            checkPremission();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            LogUtil.d(TAG,"MineWebChromeClient"+ "运行方法 onReceivedTitle:" + title);
            super.onReceivedTitle(view, title);

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            LogUtil.d(TAG,"MineWebChromeClient"+ "运行方法 onProgressChanged:" + newProgress);

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            DialogUtils.showConfirmDialog(WebActivity.this, message, v -> {
                        result.confirm();
                        v.dismiss();
                    },
                    v -> {
                        result.cancel();
                        v.dismiss();
                    });
            LogUtil.d(TAG,"MineWebChromeClient"+"运行方法 onJsAlert:" + url + " message:" +message);
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   JsResult result) {
            LogUtil.d(TAG,"MineWebChromeClient"+"运行方法 onJsConfirm:" + url + "  message:" + message);
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, JsPromptResult result) {
            LogUtil.d(TAG,"MineWebChromeClient" + "运行方法 onJsPrompt:" + url + " message:" + message);
            return true;
        }


    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mContainer.removeView(mWebView);
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();

    }
}
