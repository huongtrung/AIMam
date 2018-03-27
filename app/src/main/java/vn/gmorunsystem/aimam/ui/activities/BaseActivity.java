package vn.gmorunsystem.aimam.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vn.gmorunsystem.aimam.ui.customview.LoadingDialog;
import vn.gmorunsystem.aimam.utils.LanguageUtils;

public abstract class BaseActivity extends AppCompatActivity {
    public Unbinder mUnbinder;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogApi();
        LanguageUtils.changeLocale(getResources());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initDialogApi() {
        loadingDialog = new LoadingDialog(this);
    }

    public void showProgressBar() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showDialogChecked();
        }

    }

    public void hideProgressBar() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismissDialogChecked();
        }
    }

    public void replaceFragment(int container, Fragment fragment, boolean isAddToBackStack) {
        replaceFragment(container, fragment, null, isAddToBackStack);
    }

    public void replaceFragment(int container, Fragment fragment, String tag, boolean isAddToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        if (tag != null) {
            Fragment f = manager.findFragmentByTag(tag);
            if (f != null) {
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

    protected void addFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack("")
                .commit();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
