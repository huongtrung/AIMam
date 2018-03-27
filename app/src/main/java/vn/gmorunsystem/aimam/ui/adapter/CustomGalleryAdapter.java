package vn.gmorunsystem.aimam.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.CustomImageGalleryItemViewHolder;


public class CustomGalleryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int allItem;
    private boolean[] thumbnailsSelection;
    private int imageId[];
    private int selectedCount = 0;

    public CustomGalleryAdapter(Context context, int count, boolean[] thumbnailsSelected, int[] imageIds) {
        mContext = context;
        allItem = count;
        thumbnailsSelection = thumbnailsSelected;
        imageId = imageIds;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return allItem;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final CustomImageGalleryItemViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.custom_gallery_item, null);
            holder = new CustomImageGalleryItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (CustomImageGalleryItemViewHolder) view.getTag();
        }
        holder.chkImage.setId(position);
        holder.imgThumb.setId(position);
        holder.chkImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int pos = cb.getId();
                if (thumbnailsSelection[pos]) {
                    cb.setChecked(false);
                    thumbnailsSelection[pos] = false;
                    selectedCount--;
                } else {
                    if (selectedCount == 5) {
                        Toast.makeText(mContext, R.string.select_five_image, Toast.LENGTH_SHORT).show();
                        cb.setChecked(false);
                        return;
                    }
                    cb.setChecked(true);
                    thumbnailsSelection[pos] = true;
                    selectedCount++;
                }
            }
        });

        holder.imgThumb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int pos = holder.chkImage.getId();
                if (thumbnailsSelection[pos]) {
                    holder.chkImage.setChecked(false);
                    thumbnailsSelection[pos] = false;
                    selectedCount--;
                } else {
                    if (selectedCount == 5) {
                        Toast.makeText(mContext, R.string.select_five_image, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holder.chkImage.setChecked(true);
                    thumbnailsSelection[pos] = true;
                    selectedCount++;
                }
            }
        });
        try {
            Glide.with(mContext)
                    .load(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageId[position])))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgThumb);
        } catch (Throwable e) {
        }
        holder.chkImage.setChecked(thumbnailsSelection[position]);
        holder.id = position;
        return view;
    }
}
