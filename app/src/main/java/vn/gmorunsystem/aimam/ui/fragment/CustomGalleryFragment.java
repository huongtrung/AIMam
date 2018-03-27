package vn.gmorunsystem.aimam.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.ui.adapter.CustomGalleryAdapter;

public class CustomGalleryFragment extends BaseFragment {
    GridView grdImages;
    Button btnSelect;

    CustomGalleryAdapter customGalleryAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsSelection;
    private int imageId[];
    private int count;
    private Cursor imageCursor;
    ArrayList<String> imagesPathList;

    public static CustomGalleryFragment newInstance() {
        CustomGalleryFragment newFragment = new CustomGalleryFragment();
        return newFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_gallery;
    }

    @Override
    protected void initView(View root) {
        getMainActivity().showBottomBarAndSearchIcon();
        setScreenTitle(R.string.title_photo_library);
        getMainActivity().hideBottomBarAndSearchIcon();
    }

    @Override
    protected void initData() {
        grdImages = (GridView) mView.findViewById(R.id.grdImages);
        btnSelect = (Button) mView.findViewById(R.id.btnSelect);
        imagesPathList = new ArrayList<>();

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        imageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy);
        int image_column_index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        this.count = imageCursor.getCount();
        this.arrPath = new String[this.count];
        imageId = new int[count];
        this.thumbnailsSelection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imageCursor.moveToPosition(i);
            imageId[i] = imageCursor.getInt(image_column_index);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = imageCursor.getString(dataColumnIndex);
        }

        customGalleryAdapter = new CustomGalleryAdapter(getActivity().getApplicationContext(), count, thumbnailsSelection, imageId);
        grdImages.setAdapter(customGalleryAdapter);
        imageCursor.close();

        btnSelect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final int len = thumbnailsSelection.length;
                int cnt = 0;
                for (int i = 0; i < len; i++) {
                    if (thumbnailsSelection[i]) {
                        cnt++;
                        imagesPathList.add(arrPath[i]);
                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getActivity(), R.string.select_one_image, Toast.LENGTH_SHORT).show();
                } else {
                    replaceFragment(R.id.container, UploadPhotoFragment.newInstance(imagesPathList));
                }
            }
        });
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }
}
