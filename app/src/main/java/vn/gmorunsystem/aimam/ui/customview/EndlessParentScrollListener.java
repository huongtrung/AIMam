package vn.gmorunsystem.aimam.ui.customview;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HuongTrung on 08/24/2017.
 */

public abstract class EndlessParentScrollListener implements NestedScrollView.OnScrollChangeListener {
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;
    private int visibleThresholdDistance = 300;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessParentScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrollChange(NestedScrollView scrollView, int x, int y, int oldx, int oldy) {
        View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int distanceToEnd = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        int totalItemCount = mLayoutManager.getItemCount();
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && distanceToEnd <= visibleThresholdDistance) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}
