package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.BuildConfig;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_vesion_no)
    TextView tvVersionNo;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.nav_about);
        getMainActivity().hideBottomBarAndSearchIcon();
        handleUserInfo();
        initAppVersion();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void handleUserInfo() {
        String avatarURL = SharedPrefUtils.getAvatarUrl();
        String userName = SharedPrefUtils.getUserName();


        ImageLoader.loadImage(getContext(), R.drawable.default_img, avatarURL, civAvatar);
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(userName), tvUserName);
    }

    private void initAppVersion() {
        String versionName = BuildConfig.VERSION_NAME;
        StringUtil.displayText(versionName, tvVersionNo);
    }

}
