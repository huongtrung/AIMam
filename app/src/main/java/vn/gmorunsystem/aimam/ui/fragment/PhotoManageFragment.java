package vn.gmorunsystem.aimam.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import vn.gmorunsystem.aimam.BuildConfig;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.ImagePickerUtil;

/**
 * Created by Veteran Commander on 5/22/2017.
 */

public class PhotoManageFragment extends BaseFragment implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 111;
    public static final int REQUEST_PERMISSION_READ = 222;

    @BindView(R.id.layout_bt_camera)
    LinearLayout lyCamera;
    @BindView(R.id.layout_bt_photo)
    LinearLayout lyPhoto;
    @BindView(R.id.layout_content_manage)
    LinearLayout lyContentUpload;
    @BindView(R.id.iv_reviewPhoto)
    ImageView ivReviewPhoto;
    @BindView(R.id.rl_review_photo)
    RelativeLayout rlReviewPhoto;
    @BindView(R.id.bt_cancel)
    Button btnCancel;
    @BindView(R.id.btn_save_img)
    Button btnSaveImage;

    Uri outPutUri;
    Bitmap temp;

    public static PhotoManageFragment newInstance() {
        PhotoManageFragment newFragment = new PhotoManageFragment();
        return newFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_manage;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.app_name);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().showBottomBarAndSearchIcon();
    }

    @Override
    protected void initData() {
        AppUtils.makeDir(APPConstant.DIRECTORY);
        lyCamera.setOnClickListener(this);
        lyPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSaveImage.setOnClickListener(this);

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_bt_camera:
                callCamera();
                break;
            case R.id.layout_bt_photo:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {// if user check never ask again
                        DialogUtil.showDialogConfirmExit(getActivity(), getString(R.string.request_permission_mess),
                                getString(R.string.title_yes), getString(R.string.title_no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                REQUEST_PERMISSION_READ);
                                    }
                                });
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ);
                } else
                    callPhoto();
                break;
            case R.id.bt_cancel:
                callCamera();
                break;
            case R.id.btn_save_img:
                saveTakenImage(temp);
                DialogUtil.showDialogFull(getActivity(), getString(R.string.take_photo_success), getString(R.string.take_photo_more), getString(R.string.title_yes), getString(R.string.title_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callCamera();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callPhoto();
                            }
                        });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == getActivity().RESULT_OK) {
            lyContentUpload.setVisibility(View.GONE);
            rlReviewPhoto.setVisibility(View.VISIBLE);
            try {
                temp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outPutUri);
                ivReviewPhoto.setImageBitmap(temp);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public void callCamera() {
        Intent getCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImagePickerUtil imagePickerUtil = new ImagePickerUtil();
        outPutUri = Uri.fromFile(imagePickerUtil.createFileUri(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            outPutUri = FileProvider.getUriForFile(getActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    imagePickerUtil.createFileUri(getActivity()));
        }
        getCamera.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        startActivityForResult(getCamera, REQUEST_CAMERA);
    }

    public void callPhoto() {
        replaceFragment(R.id.container, CustomGalleryFragment.newInstance());

    }

    public File saveTakenImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = getString(R.string.app_name) + timeStamp;
        File imageFile = new File(AppUtils.makeDir(APPConstant.DIRECTORY), fileName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = imageFile.getAbsolutePath();
        registAndroidDB(filePath);
        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(R.id.container, CustomGalleryFragment.newInstance(), null);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                    manager.executePendingTransactions();
                } else {
                    DialogUtil.showDialog(getActivity(), getString(R.string.dialog_must_have_read_permission));
                }
                return;
            }

        }
    }
}
