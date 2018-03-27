package vn.gmorunsystem.aimam.ui.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.activities.MainActivity;
import vn.gmorunsystem.aimam.ui.customview.LoadingDialog;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.KeyboardUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.UiUtil;

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected View mView;
    public App mApp;

    protected ViewGroup fragmentViewParent;
    LoadingDialog loadingDialog;

    private Unbinder mUnbinder;
    private boolean isRootViewExist;
    private String screenTitle;

    private Uri imageDownloadedUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DebugLog.e("Lifecycle " + this.getClass().getSimpleName());
        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        initCommonViews(rootView);
        mView = inflater.inflate(getLayoutId(), container, false);
        fragmentViewParent.addView(mView);
        return rootView;
    }

    private void initCommonViews(View rootView) {
        fragmentViewParent = (ViewGroup) rootView.findViewById(R.id.fragmentViewParent);
        initDialogApi();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        isRootViewExist = true;
        if (getArguments() != null) {
            getArgument(getArguments());
        }
        initView(rootView);
        initData();
    }

    abstract protected int getLayoutId();

    abstract protected void initView(View root);

    abstract protected void initData();

    abstract protected void getArgument(Bundle bundle);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        hideProgressBar();
        hideSoftKeyboard();
        isRootViewExist = false;
    }

    @Override
    public void onDestroy() {
        fragmentViewParent = null;
        rootView = null;
        super.onDestroy();
    }

    private void initDialogApi() {
        loadingDialog = new LoadingDialog(getContext());
    }

    protected void showProgressBar() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showDialogChecked();
        }

    }

    protected void hideProgressBar() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismissDialogChecked();
        }
    }

    protected void hideSoftKeyboard() {
        KeyboardUtil.hideSoftKeyboard(getActivity());
    }

    protected boolean isRootViewExist() {
        return isRootViewExist;
    }

    public String getScreenTitle() {
        return screenTitle;
    }

    public void setScreenTitle(String screenTitle) {
        this.screenTitle = screenTitle;
        getMainActivity().setTitle(screenTitle);
    }

    public void setScreenTitle(int screenTitleId) {
        this.screenTitle = getString(screenTitleId);
        getMainActivity().setTitle(screenTitle);
    }

    public MainActivity getMainActivity() {

        if (getActivity() instanceof MainActivity) {
            return (MainActivity) getActivity();
        } else {
            throw new RuntimeException("This fragment was not attached on NewsFragment");
        }
    }

    protected void showOrHideView(View view) {
        if (view.getVisibility() == View.VISIBLE)
            UiUtil.hideView(view, true);
        else
            UiUtil.showView(view);
    }

    public void replaceFragment(int container, Fragment fragment, boolean isAddToBackStack) {
        replaceFragment(container, fragment, null, isAddToBackStack);
    }

    public void replaceFragment(int container, Fragment fragment) {
        replaceFragment(container, fragment, null, true);
    }

    public void replaceFragment(int container, Fragment fragment, String tag, boolean isAddToBackStack) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (tag != null) {
            Fragment f = manager.findFragmentByTag(tag);
            if (f != null) {
                // back to fragment currently in backstack
                manager.popBackStack(tag, 0);
                return;
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public void addFragment(int container, Fragment fragment, String tag) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * This method replace fragment and clear all back stack
     *
     * @param container fragment container
     * @param fragment  new fragment
     */
    public void replaceAndClearFragment(int container, Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear action
        replaceFragment(container, fragment);
    }

    public void replaceFragmentDontBack(int container, Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment);
        transaction.commit();
    }


    protected void handleBack() {
        int backStackCnt = getFragmentManager().getBackStackEntryCount();
        if (backStackCnt > 1) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().onBackPressed();
        }

    }

    protected void handleBackByTAG(String tag) {
        int backStackCnt = getFragmentManager().getBackStackEntryCount();
        if (backStackCnt > 1) {
            // "0" mean : not pop the fragment with this tag
            getFragmentManager().popBackStack(tag, 0);
        } else {
            getActivity().onBackPressed();
        }
    }

    public void registAndroidDB(String path) {
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getActivity().getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put("_data", path);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public boolean checkInstagramIsInstall() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().
                    getApplicationInfo(APPConstant.INSTAGRAM_PACKAGE, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void shareInFacebook(String url) {
        if (url != null && !url.isEmpty()) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(url))
                    .build();
            ShareDialog.show(getActivity(), content);
        } else DialogUtil.showDialog(getActivity(), getString(R.string.can_not_get_url_to_share));

    }

    public void shareInInstagram(String url, String fileName) {
        if (url != null && !url.isEmpty()) {
            if (checkInstagramIsInstall()) {
                if (new File(APPConstant.DIRECTORY_DOWNLOAD + "/" + fileName + ".jpg").isFile()) {
                    shareToInstagramApp(Uri.fromFile(new File(APPConstant.DIRECTORY_DOWNLOAD, fileName + ".jpg")));
                } else
                    downloadImageForShareInstagram(url, fileName);
            } else {
                DialogUtil.showDialogFull(getActivity(), getString(R.string.error), getString(R.string.instagram_not_install_mess), getString(R.string.title_yes), getString(R.string.title_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                IntentUtil.openInstallApp(getActivity(), APPConstant.INSTAGRAM_PACKAGE);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

            }
        } else DialogUtil.showDialog(getActivity(), getString(R.string.can_not_get_url_to_share));
    }

    public void downloadImageForShareInstagram(String imgUrl, final String fileName) {
        AppUtils.makeDir(APPConstant.DIRECTORY_DOWNLOAD);
        ImageRequest request = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageDownloadedUri = Uri.fromFile(saveDownloadImage(bitmap, fileName));
                        hideProgressBar();
                        shareToInstagramApp(imageDownloadedUri);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
//                        DialogUtil.showDialog(getActivity(), error.getMessage());
                        DialogUtil.showDialog(getActivity(), getString(R.string.can_not_get_url_to_share));
                    }
                });
        NetworkUtils.getInstance(App.getInstance()).addToRequestQueue(request);
        showProgressBar();

    }

    public File saveDownloadImage(Bitmap bitmap, String fileName) {
        File imageFile = new File(APPConstant.DIRECTORY_DOWNLOAD, "/" + fileName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = imageFile.getAbsolutePath();
        registAndroidDB(filePath);
        return imageFile;
    }

    private void shareToInstagramApp(Uri uri) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setPackage(APPConstant.INSTAGRAM_PACKAGE);
        startActivity(Intent.createChooser(share, "Share to"));
    }
}
