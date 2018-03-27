package vn.gmorunsystem.aimam.callback;

/**
 * Created by adm on 8/31/2017.
 */

public interface OnChangeMenuItemListener {

    void onRepairItem(OnMenuItemRepairListener listener);

    void updateItem(int position, int itemId);
}
