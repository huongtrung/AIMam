package vn.gmorunsystem.aimam.ui.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.DeleteItemReviewRequest;
import vn.gmorunsystem.aimam.apis.request.GetShopReviewRequest;
import vn.gmorunsystem.aimam.apis.request.SendShopReviewRequest;
import vn.gmorunsystem.aimam.apis.request.UnUsefulRequest;
import vn.gmorunsystem.aimam.apis.request.UpdateShopReviewRequest;
import vn.gmorunsystem.aimam.apis.request.UsefulRequest;
import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.response.SendShopReviewResponse;
import vn.gmorunsystem.aimam.apis.response.ShopReviewResponse;
import vn.gmorunsystem.aimam.apis.response.UsefulResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ReviewBean;
import vn.gmorunsystem.aimam.callback.ImageUrlToSocialShareCallBack;
import vn.gmorunsystem.aimam.callback.OnRecyclerViewItemLongClick;
import vn.gmorunsystem.aimam.callback.OnViewInReviewRecyclerClicked;
import vn.gmorunsystem.aimam.callback.SubscribedStatusCallBack;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.ui.adapter.CommentListAdapter;
import vn.gmorunsystem.aimam.ui.customview.EndlessParentScrollListener;
import vn.gmorunsystem.aimam.utils.AnimationUtil;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.EventBusHelper;
import vn.gmorunsystem.aimam.utils.KeyboardUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.utils.DialogUtil.showDialog;


public class ShopReviewFragment extends BaseFragment implements OnViewInReviewRecyclerClicked {
    private static final int DEFAULT_PAGER = 1;
    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_ZERO = 0;
    private static final String SHOP_ID = "SHOP_ID";

    @BindView(R.id.rc_cmList)
    RecyclerView rvComment;
    @BindView(R.id.iv_review_cm)
    ImageView ivReviewCm;
    @BindView(R.id.iv_refresh_code)
    ImageView ivRefreshCode;
    @BindView(R.id.layout_review)
    RelativeLayout rlReview;
    @BindView(R.id.layout_write_cm)
    RelativeLayout rlWriteCm;
    @BindView(R.id.bt_cancel)
    Button btnCancel;
    @BindView(R.id.rtb_rate)
    SimpleRatingBar ratingBar;
    @BindView(R.id.rtb_review_rate)
    SimpleRatingBar reviewRatingBar;
    @BindView(R.id.et_input_code)
    EditText edtInputCode;
    @BindView(R.id.edt_write_cm)
    EditText edtWriteCm;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_go_to_shop)
    TextView tvGoToShop;
    @BindView(R.id.tv_review_description)
    TextView tvDescription;
    @BindView(R.id.tv_count_view)
    TextView tvViewCount;
    @BindView(R.id.tv_count_like)
    TextView tvLikeCount;
    @BindView(R.id.tv_numCm)
    TextView tvNumCm;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;

    List<ReviewBean> mReviewList = new ArrayList<>();
    ReviewBean mReviewItem;
    CommentListAdapter mCommentAdapter;
    ShopReviewResponse mShopReviewResponse;

    int mPage = 1;
    int mPosition;
    int shopId;
    int reviewId;
    int mTotalPages;
    int commentCount = 0;
    String mContent;
    String mCode;
    String mCheckCode;
    boolean isUpdate = false;
    String urlShareToSocialNetwork;
    String fileName;

    public static ShopReviewFragment newInstance(int shopId) {
        ShopReviewFragment fragment = new ShopReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_review;
    }

    @Override
    protected void initView(View root) {
        EventBusHelper.register(this);
        initShopReview();
        setHideOrShowCmtList(true);
        edtInputCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        edtWriteCm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    KeyboardUtil.dismissKeyboard(view);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            shopId = getArguments().getInt(SHOP_ID);
            if (mShopReviewResponse == null)
                getShopReviewRequest(false);
            else
                handleShopReviewResponse(mShopReviewResponse);
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    public void getShopReviewRequest(final boolean isLoadMore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (isLoadMore) {
                showProgressBar();
                mPage++;
            } else
                mPage = DEFAULT_PAGER;
            GetShopReviewRequest request = new GetShopReviewRequest(shopId, mPage);
            request.setRequestCallBack(new ApiObjectCallBack<ShopReviewResponse>() {
                @Override
                public void onSuccess(ShopReviewResponse data) {
                    if (isLoadMore) {
                        hideProgressBar();
                    }
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        mShopReviewResponse = data;
                        commentCount = data.total;
                        handleShopReviewResponse(data);
                    } else DialogUtil.showDialog(getActivity(), getString(R.string.not_have_data));
                }

                @Override
                public void onFail(int failCode, String message) {
                    if (isLoadMore)
                        hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void handleShopReviewResponse(ShopReviewResponse data) {
        if (data.reviews != null && data.reviews.size() > 0) {
            mTotalPages = data.totalPages;
            if (mPage == DEFAULT_PAGER) {
                mReviewList = data.reviews;
            } else {
                mCommentAdapter.addCommentList(data.reviews);
            }
        }
        setUpCommentList();
//        urlShareToSocialNetwork = data.avatarImageUrl;
        fileName = "ShopId_" + data.shopId + "_" + "ItemId_" + reviewId;
        reviewRatingBar.setRating(data.vote);
        StringUtil.displayText(data.name, tvGoToShop);
        StringUtil.displayText(data.description, tvDescription);
        StringUtil.displayText(getString(R.string.txt_view) + data.view, tvViewCount);
        StringUtil.displayText(getString(R.string.txt_subscribe) + data.like, tvLikeCount);
    }

    public void setUpCommentList() {
        rvComment.setNestedScrollingEnabled(false);
        rvComment.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvComment.setLayoutManager(layoutManager);
        mCommentAdapter = new CommentListAdapter(mReviewList);
        rvComment.setAdapter(mCommentAdapter);

        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < mTotalPages) {
                    getShopReviewRequest(true);
                }
            }
        });

        mCommentAdapter.setOnViewInReviewRecyclerClicked(this);
        mCommentAdapter.setOnRecyclerViewItemLongClick(new OnRecyclerViewItemLongClick() {
            @Override
            public void onItemLongClick(View view, final int position) {
                if (mReviewList.get(position).userId.equals(SharedPrefUtils.getUserId())) {
                    DialogUtil.showDialogFull(getActivity(), getString(R.string.change_rv), getString(R.string.ask_ser_edt_rv),
                            getString(R.string.update), getString(R.string.delete),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mPosition = position;
                                    setHideOrShowCmtList(false);
                                    ratingBar.setRating(mReviewList.get(position).vote);
                                    StringUtil.displayText(mReviewList.get(position).content, edtWriteCm);
                                    reviewId = mReviewList.get(position).id;
                                    isUpdate = true;
                                    clearInputCode();
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteReview(mReviewList.get(position).id, position);
                                }
                            });
                } else {
                    ToastUtil.makeText(getContext(), getString(R.string.not_user_cm));
                }
            }
        });
        StringUtil.displayText(String.format(getString(R.string.title_comment), commentCount), tvNumCm);
    }

    private void clearInputCode() {
        edtInputCode.setText("");
        StringUtil.displayText(StringUtil.randomString(), tvCode);
    }

    private void sendReview() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            int rating = (int) ratingBar.getRating();
            SendShopReviewRequest request = new SendShopReviewRequest(shopId, rating, mContent);
            request.setRequestCallBack(new ApiObjectCallBack<SendShopReviewResponse>() {
                @Override
                public void onSuccess(SendShopReviewResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        setHideOrShowCmtList(true);
                        resetLayoutWhenClick();
                        commentCount = commentCount + NUMBER_ONE;
                        StringUtil.displayText(String.format(getString(R.string.title_comment), commentCount), tvNumCm);
                        mCommentAdapter.addCommentItem(setDataReviewItem(data));
                    } else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    clearInputCode();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void updateReview() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UpdateShopReviewRequest request = new UpdateShopReviewRequest(shopId, ratingBar.getRating(), mContent, reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<SendShopReviewResponse>() {
                @Override
                public void onSuccess(SendShopReviewResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        isUpdate = false;
                        setHideOrShowCmtList(true);
                        resetLayoutWhenClick();
                        mCommentAdapter.updateCommentItem(mPosition, setDataReviewItem(data));
                    } else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    clearInputCode();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void deleteReview(int reviewId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            DeleteItemReviewRequest request = new DeleteItemReviewRequest(reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        commentCount = commentCount - NUMBER_ONE;
                        StringUtil.displayText(String.format(getString(R.string.title_comment), commentCount), tvNumCm);
                        mCommentAdapter.removeCommentItem(position);
                    } else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }


    @Override
    public void onUseful(int reviewId, int position, boolean isUseful) {
        if (isUseful) {
            sendUnUsefulRequest(reviewId, position);
        } else {
            sendUsefulRequest(reviewId, position);
        }
    }

    private void sendUsefulRequest(int reviewId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            UsefulRequest request = new UsefulRequest(reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<UsefulResponse>() {
                @Override
                public void onSuccess(UsefulResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        mReviewList.get(position).setUseful(mReviewList.get(position).useful + NUMBER_ONE);
                        mReviewList.get(position).setVotedUseful(NUMBER_ONE);
                        mCommentAdapter.notifyDataSetChanged();
                    } else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void sendUnUsefulRequest(int reviewId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UnUsefulRequest request = new UnUsefulRequest(reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        mReviewList.get(position).setUseful(mReviewList.get(position).useful - NUMBER_ONE);
                        mReviewList.get(position).setVotedUseful(NUMBER_ZERO);
                        mCommentAdapter.notifyDataSetChanged();
                    } else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private ReviewBean setDataReviewItem(SendShopReviewResponse data) {
        mReviewItem = new ReviewBean();
        mReviewItem.setId(data.id);
        mReviewItem.setUserName(data.userName);
        mReviewItem.setUserId(data.userId);
        mReviewItem.setUserAvartarUrl(SharedPrefUtils.getAvatarUrl());
        mReviewItem.setContent(data.content);
        mReviewItem.setUseful(data.useful);
        mReviewItem.setVote(data.vote);
        mReviewItem.setVotedUseful(NUMBER_ZERO);
        mReviewItem.setReviewDate(StringUtil.getCurrentDate());
        return mReviewItem;
    }

    private void resetLayoutWhenClick() {
        ratingBar.setRating(0);
        UiUtil.hideView(tvError, true);
        edtWriteCm.setText("");
        edtWriteCm.setBackgroundResource(R.color.gray_dark);
        edtInputCode.setBackgroundResource(R.color.gray_dark);
        clearInputCode();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SubscribedStatus(SubscribedStatusCallBack statusCallBack) {
        if (statusCallBack.isSubscribedStatus()) {
            mShopReviewResponse.setLike(mShopReviewResponse.like + NUMBER_ONE);
        } else {
            mShopReviewResponse.setLike(mShopReviewResponse.like - NUMBER_ONE);
        }
        StringUtil.displayText(getString(R.string.txt_subscribe) + mShopReviewResponse.like, tvLikeCount);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUrlToShare(ImageUrlToSocialShareCallBack callBack) {
        if (StringUtil.checkStringValid(callBack.getImageUrl())) {
            urlShareToSocialNetwork = callBack.getImageUrl();
        }
    }

    @OnClick({R.id.bt_cancel, R.id.btn_send_review, R.id.iv_refresh_code, R.id.ic_fb_review, R.id.ic_instagram_review, R.id.iv_review_cm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                setHideOrShowCmtList(true);
                resetLayoutWhenClick();
                break;
            case R.id.iv_refresh_code:
                clearInputCode();
                AnimationUtil.loadAnimationRotate(getContext(), ivRefreshCode);
                break;
            case R.id.btn_send_review:
                callApiSendOrUpdateReview();
                break;
            case R.id.ic_instagram_review:
                shareFacebookOrInstagram(false);
                break;
            case R.id.ic_fb_review:
                shareFacebookOrInstagram(true);
                break;
            case R.id.iv_review_cm:
                setHideOrShowCmtList(false);
                clearInputCode();
                break;
        }
    }

    private void shareFacebookOrInstagram(boolean isFace) {
        if (!urlShareToSocialNetwork.isEmpty()) {
            if (isFace) {
                shareInFacebook(APIConstant.WEB_URL);
            } else {
                shareInInstagram(urlShareToSocialNetwork, fileName);
            }
        } else DialogUtil.showDialog(getActivity(), getString(R.string.can_not_get_url_to_share));
    }


    private void callApiSendOrUpdateReview() {
        mContent = edtWriteCm.getText().toString().trim();
        mCode = tvCode.getText().toString().trim();
        mCheckCode = edtInputCode.getText().toString().trim();
        if (checkValidInput()) {
            if (!isUpdate) {
                sendReview();
            } else {
                updateReview();
            }
        }
    }

    private boolean checkValidInput() {
        if (StringUtil.isEmpty(mContent) || mContent.length() > 400) {
            UiUtil.showView(tvError);
            edtWriteCm.setBackgroundResource(R.drawable.bg_edit_review);
            return false;
        } else if (!mCode.equalsIgnoreCase(mCheckCode)) {
            if (!StringUtil.isEmpty(mContent) || mContent.length() < 400) {
                UiUtil.hideView(tvError, true);
                edtWriteCm.setBackgroundResource(R.color.gray_dark);
            }
            clearInputCode();
            edtInputCode.setBackgroundResource(R.drawable.bg_edit_review);
            return false;
        } else {
            UiUtil.hideView(tvError, true);
            edtWriteCm.setBackgroundResource(R.color.gray_dark);
            edtInputCode.setBackgroundResource(R.color.gray_dark);
            return true;
        }
    }

    @OnEditorAction(R.id.et_input_code)
    public boolean actionDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callApiSendOrUpdateReview();
            return true;
        }
        return false;
    }

    private void initShopReview() {
        setHideOrShowCmtList(true);
        ratingBar.setRating(0);
        ratingBar.setStepSize(1);
        ratingBar.setIndicator(false);
        reviewRatingBar.setIndicator(true);

    }

    private void setHideOrShowCmtList(boolean isShowCmt) {
        if (isShowCmt) {
            UiUtil.showView(rvComment);
            UiUtil.hideView(rlWriteCm, true);
        } else {
            UiUtil.hideView(rvComment, true);
            UiUtil.showView(rlWriteCm);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusHelper.unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.unregister(this);
    }

}
