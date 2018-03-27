package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopMenuItem;
import vn.gmorunsystem.aimam.callback.OnChangeMenuItemListener;
import vn.gmorunsystem.aimam.callback.OnItemClickListener;
import vn.gmorunsystem.aimam.callback.OnMenuItemRepairListener;
import vn.gmorunsystem.aimam.ui.activities.MainActivity;
import vn.gmorunsystem.aimam.ui.adapter.ShopMenuItemAdapter;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by Veteran Commander on 5/24/2017.
 */

public class UploadPhotoListViewHolder extends OnClickViewHolder {

    public static final int LAYOUT_ID = R.layout.photo_upload_list_item;

    private MainActivity mContext;

    @BindView(R.id.iv_upload_item)
    ImageView ivUploadImg;
    @BindView(R.id.iv_upload_item_larger)
    ImageView ivUploadImgLarge;
    @BindView(R.id.tv_ask_info)
    TextView tvAskInfo;
    @BindView(R.id.tv_repair)
    TextView tvRepair;
    @BindView(R.id.auto_complete_edt_item)
    AutoCompleteTextView autoCompleteItemTextView;
    @BindView(R.id.upload_info)
    RelativeLayout rlUploadInfo;
    @BindView(R.id.edit_upload_info)
    RelativeLayout rlEditUploadInfo;

    String filePath;
    int itemPosition;
    int menuItemId;
    ShopMenuItemAdapter shopMenuItemAdapter;
    OnChangeMenuItemListener onChangeMenuItemListener;
    OnMenuItemRepairListener repairListener;

    public void setOnChangeMenuItemListener(OnChangeMenuItemListener onChangeMenuItemListener) {
        this.onChangeMenuItemListener = onChangeMenuItemListener;
    }

    public UploadPhotoListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(MainActivity activity, String filePath, final List<ShopMenuItem> menuItems, int position) {
        mContext = activity;
        itemPosition = position;
        shopMenuItemAdapter = new ShopMenuItemAdapter(mContext, R.layout.item_menu_for_upload,
                (ArrayList<ShopMenuItem>) menuItems);
        this.filePath = filePath;
        ImageLoader.loadImageThumnail(itemView.getContext(), filePath, ivUploadImg);
        shopMenuItemAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id) {
                for (ShopMenuItem item : menuItems) {
                    if (item.getId() == id) {
                        autoCompleteItemTextView.setText(item.getName());
                        menuItemId = id;
                    }
                }
                mContext.hideSoftKeyboard();
                autoCompleteItemTextView.dismissDropDown();
            }
        });
        autoCompleteItemTextView.setThreshold(1);
        autoCompleteItemTextView.setAdapter(shopMenuItemAdapter);
        repairListener = new OnMenuItemRepairListener() {
            @Override
            public void repairListen() {
                hideRepairItemView();
            }
        };
    }

    @OnClick({R.id.tv_repair, R.id.btn_yes, R.id.btn_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_repair:
                if (onChangeMenuItemListener != null) {
                    onChangeMenuItemListener.onRepairItem(repairListener);
                }
                rlUploadInfo.setVisibility(View.GONE);
                rlEditUploadInfo.setVisibility(View.VISIBLE);
                ImageLoader.loadImageThumnail(itemView.getContext(), filePath, ivUploadImgLarge);
                break;
            case R.id.btn_yes:
                StringUtil.displayText(autoCompleteItemTextView.getText().toString(), tvAskInfo);
                if (onChangeMenuItemListener != null) {
                    onChangeMenuItemListener.updateItem(itemPosition, menuItemId);
                }
                hideRepairItemView();
                break;
            case R.id.btn_no:
                hideRepairItemView();
                break;
        }
    }

    public void hideRepairItemView() {
        rlUploadInfo.setVisibility(View.VISIBLE);
        rlEditUploadInfo.setVisibility(View.GONE);
    }


}
