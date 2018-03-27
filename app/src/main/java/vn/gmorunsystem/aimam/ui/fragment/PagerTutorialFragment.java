package vn.gmorunsystem.aimam.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by adm on 9/14/2017.
 */

public class PagerTutorialFragment extends BaseFragment {
    private static final String GREEN = "green";
    private static final String WHITE = "white";
    private static final String IMGRESID = "imgResId";
    @BindView(R.id.tv_tut_green)
    TextView tvGreen;
    @BindView(R.id.tv_tut_white)
    TextView tvWhite;
    @BindView(R.id.iv_tut)
    ImageView ivTut;

    String textWhite;
    String textGreen;
    int resId;

    public static PagerTutorialFragment newInstance(int textGreenId, int textWhiteId, int resImageId) {
        PagerTutorialFragment newFragment = new PagerTutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GREEN, textGreenId);
        bundle.putInt(WHITE, textWhiteId);
        bundle.putInt(IMGRESID, resImageId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tutorial_pagers;
    }

    @Override
    protected void initView(View root) {
        StringUtil.displayText(textGreen, tvGreen);
        StringUtil.displayText(textWhite, tvWhite);
        ivTut.setImageDrawable(getResources().getDrawable(resId));
//        ImageLoader.loadImage(getActivity(),resId,ivTut);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getArgument(Bundle bundle) {
        textGreen = getString(bundle.getInt(GREEN));
        textWhite = getString(bundle.getInt(WHITE));
        resId = bundle.getInt(IMGRESID);
    }
}
