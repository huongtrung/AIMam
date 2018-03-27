package vn.gmorunsystem.aimam.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.response.ShopStampResponse;
import vn.gmorunsystem.aimam.bean.ShopStampBean;
import vn.gmorunsystem.aimam.ui.fragment.ShopStampFragment;
import vn.gmorunsystem.aimam.utils.DialogUtil;

public class ShopStampAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private ShopStampResponse data;
    private ShopStampFragment.IGetShopStampGift iGetShopStampGift;
    private int realPos;

    public ShopStampAdapter(Context context, ShopStampResponse data, ShopStampFragment.IGetShopStampGift iGetShopStampGift) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.data = data;
        this.iGetShopStampGift = iGetShopStampGift;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = inflater.inflate(R.layout.item_shop_stamp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position < data.stampLimit) {
            ((ViewHolder) holder).ivStar.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).ivCrown.setVisibility(View.GONE);
            ((ViewHolder) holder).tvPoint.setVisibility(View.GONE);
            ((ViewHolder) holder).ivSmallCrown.setVisibility(View.GONE);
            for (final ShopStampBean bean : data.stamps) {
                realPos = bean.position - 1;
                if ((realPos) == position) {
                    final ShopStampBean finalBean = bean;
                    ((ViewHolder) holder).ivStar.setVisibility(View.GONE);
                    ((ViewHolder) holder).ivCrown.setVisibility(View.VISIBLE);
                    holder.itemView.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.showDialogFull(mContext,
                                            mContext.getString(R.string.title_stamp_ask),
                                            finalBean.description, mContext.getString(R.string.title_yes),
                                            mContext.getString(R.string.title_no),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    if (iGetShopStampGift != null) {
                                                        iGetShopStampGift.getShopStampGift(bean.position);
                                                    }
                                                }
                                            },
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            });
                    break;
                }
            }
        } else {
            ((ViewHolder) holder).ivStar.setVisibility(View.GONE);
            ((ViewHolder) holder).ivCrown.setVisibility(View.GONE);
            ((ViewHolder) holder).tvPoint.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvPoint.setText(String.valueOf(position + 1));
            for (final ShopStampBean bean : data.stamps) {
                if (realPos == position) {
                    ((ViewHolder) holder).ivSmallCrown.setVisibility(View.VISIBLE);
                    holder.itemView.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.showOkBtnDialog(mContext, mContext.getString(R.string.title_stamp_ask_not_enough), bean.description);
                                }
                            });
                    break;
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.stampLimit;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCrown;
        ImageView ivSmallCrown;
        ImageView ivStar;
        TextView tvPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCrown = (ImageView) itemView.findViewById(R.id.iv_crown);
            ivSmallCrown = (ImageView) itemView.findViewById(R.id.iv_small_crown);
            ivStar = (ImageView) itemView.findViewById(R.id.iv_star);
            tvPoint = (TextView) itemView.findViewById(R.id.tv_point);
        }
    }
}
