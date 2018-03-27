package vn.gmorunsystem.aimam.callback;

/**
 * Created by adm on 11/14/2017.
 */

public class ShopHotDataListener {

    private boolean isDataNull;

    public ShopHotDataListener(boolean isDataNull) {
        this.isDataNull = isDataNull;
    }

    public boolean checkHotData() {
        return isDataNull;
    }
}
