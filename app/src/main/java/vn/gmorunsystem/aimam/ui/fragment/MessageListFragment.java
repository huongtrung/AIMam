package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.DeleteMessageRequest;
import vn.gmorunsystem.aimam.apis.request.MessageListRequest;
import vn.gmorunsystem.aimam.apis.response.DeleteMessageResponse;
import vn.gmorunsystem.aimam.apis.response.MessageListResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.data.MessageListData;
import vn.gmorunsystem.aimam.callback.OnDeleteListener;
import vn.gmorunsystem.aimam.ui.adapter.MessageListAdapter;
import vn.gmorunsystem.aimam.ui.customview.EndlessRecyclerOnScrollListener;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class MessageListFragment extends BaseFragment {
    private static final int DEFAULT_PAGER = 1;
    @BindView(R.id.rv_message)
    RecyclerView rvMessage;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_not_data)
    TextView tvNotData;
    @BindView(R.id.adViewBanner)
    AdView adBanner;

    MessageListAdapter mMessageListAdapter;
    List<MessageListData> mMessageList;
    private int mPage = 1;
    private int mTotalPage;

    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_list;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.title_message_list);
        getMainActivity().hideBottomBarAndSearchIcon();
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessage.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_light_green));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessageListRequest(true, false);
            }
        });
    }

    @Override
    protected void initData() {
        if (mMessageList == null) {
            getMessageListRequest(false, false);
        } else {
            setUpMessageList(mMessageList);
        }
        AppUtils.loadAdBanner(adBanner,getActivity());
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void getMessageListRequest(boolean isRefresh, boolean isLoadMore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (!isRefresh)
                showProgressBar();
            if (isLoadMore)
                mPage++;
            else {
                mPage = DEFAULT_PAGER;
            }
            MessageListRequest messageListRequest = new MessageListRequest(mPage);
            messageListRequest.setRequestCallBack(new ApiObjectCallBack<MessageListResponse>() {
                @Override
                public void onSuccess(MessageListResponse response) {
                    hideProgressBar();
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    if (response != null && response.data.size() > 0) {
                        mTotalPage = response.totalPages;
                        if (mPage == DEFAULT_PAGER) {
                            setUpMessageList(response.data);
                        } else {
                            mMessageListAdapter.addMessage(response.data);
                        }
                    } else {
                        displayToEmptyList();
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            messageListRequest.execute();
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void displayToEmptyList() {
        UiUtil.showView(tvNotData);
        UiUtil.hideView(rvMessage, true);
    }

    private void setUpMessageList(final List<MessageListData> inData) {
        mMessageList = inData;
        mMessageListAdapter = new MessageListAdapter(inData);
        rvMessage.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage <= mTotalPage)
                    getMessageListRequest(false, true);
            }
        });
        mMessageListAdapter.setMode(Attributes.Mode.Single);
        rvMessage.setAdapter(mMessageListAdapter);

        mMessageListAdapter.setOnDeleteListener(new OnDeleteListener() {
            @Override
            public void onDeleteMessage(int position, int messageId) {
                callApiDeleteMessage(messageId, position);
            }
        });
    }

    private void callApiDeleteMessage(int messageId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(messageId);
            deleteMessageRequest.setRequestCallBack(new ApiObjectCallBack<DeleteMessageResponse>() {
                @Override
                public void onSuccess(DeleteMessageResponse data) {
                    hideProgressBar();
                    if (data != null && data.message.equalsIgnoreCase("ok")) {
                        mMessageList.remove(position);
                        mMessageListAdapter.notifyItemRemoved(position);
                        mMessageListAdapter.notifyItemRangeChanged(position, mMessageList.size());
                        DialogUtil.showDialog(getContext(), getString(R.string.del_message_success));
                        if (mMessageList.size() == 0) {
                            displayToEmptyList();
                        }
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);

                }
            });
            deleteMessageRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    @Override
    public void onResume() {
        if (adBanner != null) {
            adBanner.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (adBanner != null) {
            adBanner.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adBanner != null) {
            adBanner.destroy();
        }
        super.onDestroy();
    }
}

