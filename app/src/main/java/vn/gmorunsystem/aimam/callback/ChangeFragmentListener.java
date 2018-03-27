package vn.gmorunsystem.aimam.callback;

/**
 * Created by adm on 9/22/2017.
 */

public class ChangeFragmentListener {
    private boolean changeFragment;

    public ChangeFragmentListener(boolean isChangeFragment) {
        this.changeFragment = isChangeFragment;
    }

    public boolean isChangeFragmentFromSearch() {
        return changeFragment;
    }
}
