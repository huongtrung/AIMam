package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.LanguageUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.switch_notify)
    SwitchCompat switchNotify;
    @BindView(R.id.ll_language)
    LinearLayout llLanguage;
    @BindView(R.id.tv_language)
    TextView tvLanguage;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.nav_setting);
        getMainActivity().hideBottomBarAndSearchIcon();
        setUpUserInfo();
        setStatusSwitchNotify();
        switchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPrefUtils.saveStatusNotify(b);
                DebugLog.e("status share:" + SharedPrefUtils.getStatusNotify());
            }
        });

        showCurrentLanguageSetting();

        if (llLanguage != null) {
            llLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getContext(), llLanguage);
                    popup.getMenuInflater().inflate(R.menu.language_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.english:
                                    SharedPrefUtils.saveLanguageSetting(LanguageUtils.LANG_EN);
                                    break;
                                case R.id.vietnamese:
                                    SharedPrefUtils.saveLanguageSetting(LanguageUtils.LANG_VI);
                                    break;
                            }
                            LanguageUtils.changeLocale(getResources());

                            getActivity().recreate();
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        }
    }

    private void showCurrentLanguageSetting() {
        if (tvLanguage == null) {
            return;
        }

        int lang = SharedPrefUtils.getLanguageSetting();

        switch (lang) {
            case LanguageUtils.LANG_VI:
                tvLanguage.setText(R.string.vietnamese);
                break;
            case LanguageUtils.LANG_EN:
                tvLanguage.setText(R.string.english);
                break;
        }
    }

    @Override
    protected void initData() {
    }

    private void setStatusSwitchNotify() {
        switch (SharedPrefUtils.getStatusNotify()) {
            case APPConstant.FRAG_ON:
                switchNotify.setChecked(true);
                break;
            case APPConstant.FRAG_OFF:
                switchNotify.setChecked(false);
                break;
            default:
                switchNotify.setChecked(true);
                break;
        }
    }

    private void setUpUserInfo() {
        String avatarURL = SharedPrefUtils.getAvatarUrl();
        String userName = SharedPrefUtils.getUserName();

        ImageLoader.loadImage(getContext(), R.drawable.default_img, avatarURL, civAvatar);
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(userName), tvUserName);
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @OnClick(R.id.ll_feedback)
    public void gotoFeedbackScreen() {
        replaceFragment(R.id.container, FeedbackFragment.newInstance(), true);
    }
}
