package vn.gmorunsystem.aimam.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.DeleteItemReviewRequest;
import vn.gmorunsystem.aimam.apis.request.FavouriteRequest;
import vn.gmorunsystem.aimam.apis.request.ItemDetailRequest;
import vn.gmorunsystem.aimam.apis.request.SendItemReviewRequest;
import vn.gmorunsystem.aimam.apis.request.UnUsefulRequest;
import vn.gmorunsystem.aimam.apis.request.UpdateItemReviewRequest;
import vn.gmorunsystem.aimam.apis.request.UsefulRequest;
import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.response.FavouriteResponse;
import vn.gmorunsystem.aimam.apis.response.ItemDetailResponse;
import vn.gmorunsystem.aimam.apis.response.SendItemReviewResponse;
import vn.gmorunsystem.aimam.apis.response.UsefulResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ReviewBean;
import vn.gmorunsystem.aimam.bean.data.ImageBean;
import vn.gmorunsystem.aimam.callback.OnLikeOrRateReviewItemListener;
import vn.gmorunsystem.aimam.callback.OnRecyclerViewItemLongClick;
import vn.gmorunsystem.aimam.callback.OnViewInReviewRecyclerClicked;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.ui.adapter.CommentListAdapter;
import vn.gmorunsystem.aimam.ui.adapter.DetailItemImageSliderAdapter;
import vn.gmorunsystem.aimam.ui.customview.EndlessParentScrollListener;
import vn.gmorunsystem.aimam.utils.AnimationUtil;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.KeyboardUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.utils.DialogUtil.showDialog;

/**
 * Created by Veteran Commander on 6/15/2017.
 */

public class DetailItemFragment extends BaseFragment implements OnViewInReviewRecyclerClicked {
    private static final String ITEM_ID = "ITEM_ID";
    private static final String FROM_NEWS = "FROM_NEWS";
    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_ZERO = 0;
    private static final int DEFAULT_PAGER = 1;

    @BindView(R.id.slide_pager)
    ViewPager vpSlider;
    @BindView(R.id.pager_indicator)
    CirclePageIndicator indicatorSlider;
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
    @BindView(R.id.tv_review_description)
    TextView tvDescription;
    @BindView(R.id.tv_go_to_shop)
    TextView tvGoToShop;
    @BindView(R.id.tv_count_view)
    TextView tvViewCount;
    @BindView(R.id.tv_count_like)
    TextView tvLikeCount;
    @BindView(R.id.tv_numCm)
    TextView tvNumCm;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    List<ReviewBean> mReviewList = new ArrayList<>();
    ReviewBean mReviewItem;
    CommentListAdapter mCommentAdapter;
    DetailItemImageSliderAdapter itemImageAdapter;
    ItemDetailResponse itemDetailResponse;

    int mPage = 1;
    boolean isUpdate = false;
    boolean isFromNewFeed = false;
    int mItemId;
    int reviewId;
    int shopId;
    String mContent;
    String mCode;
    String mCheckCode;
    String urlShareToSocialNetwork;
    String fileName;
    int mTotalPage;
    int mPosition;
    int commentCount = 0;

    OnLikeOrRateReviewItemListener mListener;

    public DetailItemFragment() {
    }

    public DetailItemFragment(OnLikeOrRateReviewItemListener mListener) {
        this.mListener = mListener;
    }

    public static DetailItemFragment newInstance(int itemId) {
        DetailItemFragment fragment = new DetailItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_ID, itemId);
        bundle.putBoolean(FROM_NEWS,true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DetailItemFragment newInstance(int itemId, OnLikeOrRateReviewItemListener listener) {
        DetailItemFragment fragment = new DetailItemFragment(listener);
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_ID, itemId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_item;
    }

    @Override
    protected void initView(View root) {
        edtInputCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        initDetailItem();
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
        if (itemDetailResponse == null) {
            getDetailItemRequest(false);
        } else {
            handleItemData(itemDetailResponse);
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {
        mItemId = bundle.getInt(ITEM_ID);
        if (bundle.containsKey(FROM_NEWS)){
            isFromNewFeed = bundle.getBoolean(FROM_NEWS);
        }
    }

    public void getDetailItemRequest(boolean isLoadMore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            if (isLoadMore) {
                mPage++;
            } else {
                mPage = DEFAULT_PAGER;
            }
            ItemDetailRequest itemDetailRequest = new ItemDetailRequest(mItemId, mPage);
            itemDetailRequest.setRequestCallBack(new ApiObjectCallBack<ItemDetailResponse>() {
                @Override
                public void onSuccess(ItemDetailResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        itemDetailResponse = data;
                        commentCount = data.total;
                        handleItemData(data);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            itemDetailRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void handleItemData(ItemDetailResponse data) {
        mTotalPage = data.totalPages;
        shopId = data.item.shopId;
        setUpCommentList();
        if (data.itemReviews != null && data.itemReviews.size() > 0) {
            if (mPage == DEFAULT_PAGER) {
                mReviewList = data.itemReviews;
            } else {
                mCommentAdapter.addCommentList(data.itemReviews);
                mCommentAdapter.notifyDataSetChanged();
            }
        }
        if (data.item.images != null && data.item.images.size() > 0) {
            setUpSlideView(data.item.images);
        }
        urlShareToSocialNetwork = data.item.images.get(0).avatarImageUrl;
        fileName = "ShopId_" + shopId + "_" + "ItemId_" + mItemId;
        setScreenTitle(data.item.name);
        StringUtil.displayText(data.item.shopName,tvGoToShop);
        StringUtil.displayText(data.item.description, tvDescription);
        StringUtil.displayText(getString(R.string.txt_view) + data.item.view, tvViewCount);
        StringUtil.displayText(getString(R.string.txt_like) + data.item.like, tvLikeCount);
        reviewRatingBar.setRating(data.item.vote);
        if (mListener != null) {
            mListener.upDateLikeAndRate(mItemId, data.item.view, data.item.like, data.item.vote);
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

    public void setUpCommentList() {
        rvComment.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvComment.setLayoutManager(layoutManager);
        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < mTotalPage) {
                    getDetailItemRequest(true);
                }
            }
        });

        mCommentAdapter = new CommentListAdapter(mReviewList);
        rvComment.setAdapter(mCommentAdapter);
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
                                    setHideOrShowCmtList(false);
                                    mPosition = position;
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

    private void sendReview() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            int rating = (int) ratingBar.getRating();
            SendItemReviewRequest request = new SendItemReviewRequest(mItemId, rating, mContent);
            request.setRequestCallBack(new ApiObjectCallBack<SendItemReviewResponse>() {
                @Override
                public void onSuccess(SendItemReviewResponse data) {
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
            UpdateItemReviewRequest request = new UpdateItemReviewRequest(mItemId, ratingBar.getRating(), mContent, reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<SendItemReviewResponse>() {
                @Override
                public void onSuccess(SendItemReviewResponse data) {
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
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void deleteReview(int reviewId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            final DeleteItemReviewRequest request = new DeleteItemReviewRequest(reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        commentCount = commentCount - NUMBER_ONE;
                        StringUtil.displayText(String.format(getString(R.string.title_comment), commentCount), tvNumCm);
                        mCommentAdapter.removeCommentItem(position);
                    } else
                        showDialog(getContext(), getString(R.string.error_server));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }


    private void callFavouriteRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            FavouriteRequest favouriteRequest = new FavouriteRequest(mItemId);
            favouriteRequest.setRequestCallBack(new ApiObjectCallBack<FavouriteResponse>() {
                @Override
                public void onSuccess(FavouriteResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        DialogUtil.showDialog(getContext(), getString(R.string.text_success_favorite));
                        itemDetailResponse.item.setLike(itemDetailResponse.item.like + NUMBER_ONE);
                        handleItemData(itemDetailResponse);
                    } else
                        showDialog(getContext(), getString(R.string.error_server));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            favouriteRequest.execute();
            showProgressBar();
        } else DialogUtil.showDialog(getContext(), getString(R.string.no_connect_internet));
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
            UsefulRequest request = new UsefulRequest(reviewId);
            request.setRequestCallBack(new ApiObjectCallBack<UsefulResponse>() {
                @Override
                public void onSuccess(UsefulResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        mReviewList.get(position).setUseful(mReviewList.get(position).useful + NUMBER_ONE);
                        mReviewList.get(position).setVotedUseful(NUMBER_ONE);
                        mCommentAdapter.notifyDataSetChanged();
                    } else
                        showDialog(getContext(), getString(R.string.error_server));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
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
                    } else
                        showDialog(getContext(), getString(R.string.error_server));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private ReviewBean setDataReviewItem(SendItemReviewResponse data) {
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


    @OnClick({R.id.iv_review_cm, R.id.bt_cancel, R.id.btn_send_review, R.id.iv_refresh_code,
            R.id.ic_fb_review, R.id.ic_instagram_review, R.id.bt_like,R.id.tv_go_to_shop})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                setHideOrShowCmtList(true);
                resetLayoutWhenClick();
                break;
            case R.id.iv_review_cm:
                setHideOrShowCmtList(false);
                clearInputCode();
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
            case R.id.bt_like:
                callFavouriteRequest();
                break;
            case  R.id.tv_go_to_shop:
                if (isFromNewFeed){
                    replaceFragment(R.id.container,ShopMainFragment.newInstance(shopId));
                } else {
                    getActivity().onBackPressed();
                }
        }
    }

    private void setUpSlideView(List<ImageBean> list) {
        itemImageAdapter = new DetailItemImageSliderAdapter(getActivity(), list);
        vpSlider.setAdapter(itemImageAdapter);
        indicatorSlider.setViewPager(vpSlider);
    }

    private void resetLayoutWhenClick() {
        edtWriteCm.setText("");
        setHideOrShowCmtList(true);
        ratingBar.setRating(0);
        clearInputCode();
    }

    private void initDetailItem() {
        edtInputCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        setHideOrShowCmtList(true);
        ratingBar.setRating(0);
        ratingBar.setStepSize(1);
        reviewRatingBar.setIndicator(true);
    }

    private void setHideOrShowCmtList(boolean isShowCmt) {
        if (isShowCmt) {
            UiUtil.showView(rlReview);
            UiUtil.hideView(rlWriteCm, true);
        } else {
            UiUtil.hideView(rlReview, true);
            UiUtil.showView(rlWriteCm);
        }
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

    private void shareFacebookOrInstagram(boolean isFace) {
        if (!urlShareToSocialNetwork.isEmpty()) {
            if (isFace) {
                shareInFacebook(APIConstant.WEB_URL);
            } else {
                shareInInstagram(urlShareToSocialNetwork, fileName);
            }
        } else DialogUtil.showDialog(getActivity(), getString(R.string.can_not_get_url_to_share));
    }

    private void clearInputCode() {
        edtInputCode.setText("");
        StringUtil.displayText(StringUtil.randomString(), tvCode);
    }

}
