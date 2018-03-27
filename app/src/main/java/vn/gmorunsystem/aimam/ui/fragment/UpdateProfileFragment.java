package vn.gmorunsystem.aimam.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.UpdateProfileRequest;
import vn.gmorunsystem.aimam.apis.request.UpdateProfileWithOutImage;
import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.data.ProfileData;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Veteran Commander on 5/17/2017.
 */

public class UpdateProfileFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    public static final int REQUEST_GET_IMG = 1;
    public static final String REGEX = "^([A-Za-z0-9\\s]+)";
    public static final int MALE = 1;
    public static final int FEMALE = 0;
    private static final String PROFILE = "profile data";

    File imgAvatar;
    ProfileData profileData;

    @BindView(R.id.iv_edit_ava)
    CircleImageView ivEditAva;
    @BindView(R.id.edt_fullname)
    EditText edtFullName;
    @BindView(R.id.tv_error_user_name)
    TextView tvErrorName;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_web)
    EditText edtWeb;
    @BindView(R.id.edt_birthday)
    EditText edtBirthday;
    @BindView(R.id.edt_work)
    EditText edtWork;
    @BindView(R.id.edt_intro)
    EditText edtIntro;
    @BindView(R.id.spinner_gender)
    Spinner spinnerGender;
    String[] gender;
    Map<String, String> params;
    Map<String, File> fileMap;

    public static UpdateProfileFragment newInstance(ProfileData data) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROFILE, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_profile;
    }

    @OnClick(R.id.tv_changephoto)
    public void changeImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_GET_IMG);
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.title_edit_pf);
        getMainActivity().hideBottomBarAndSearchIcon();
        gender = new String[]{getString(R.string.male), getString(R.string.female)};
        params = new HashMap<>();
        fileMap = new HashMap<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_gender_items, gender);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(this);
        edtBirthday.setFocusable(false);
    }

    @Override
    protected void initData() {
        ImageLoader.loadImage(getActivity(), R.drawable.default_img, SharedPrefUtils.getAvatarUrl(), ivEditAva);
    }

    @Override
    protected void getArgument(Bundle bundle) {
        profileData = bundle.getParcelable(PROFILE);
        if (StringUtil.checkStringValid(profileData.fullName)) {
            edtFullName.setText(profileData.fullName);
        }
        if (StringUtil.checkStringValid(profileData.address)) {
            edtAddress.setText(profileData.address);
        }
        if (StringUtil.checkStringValid(profileData.birthday)) {
            edtBirthday.setText(profileData.birthday);
        }
        if (StringUtil.checkStringValid(profileData.work)) {
            edtWork.setText(profileData.work);
        }
        if (StringUtil.checkStringValid(profileData.intro)) {
            edtIntro.setText(profileData.intro);
        }
        if (StringUtil.checkStringValid(profileData.website)) {
            edtWeb.setText(profileData.website);
        }
        if (profileData.gender != null) {
            if (profileData.gender == FEMALE) {
                spinnerGender.setSelection(MALE);
            } else if (profileData.gender == MALE) {
                spinnerGender.setSelection(FEMALE);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_GET_IMG) {
            Bitmap bm = null;
            if (data != null) {
                //Get image
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                        bm = Bitmap.createScaledBitmap(bm, ivEditAva.getWidth(),ivEditAva.getHeight(),true);
                    }
                    ivEditAva.setImageBitmap(bm);
                    imgAvatar = new File(getRealPathFromDocumentUri(getActivity(), data.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.edt_birthday)
    public void getDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int mYear = c.get(Calendar.YEAR) - 6;
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.YEAR, -6);
        long maxDate = c.getTime().getTime();
        if (StringUtil.checkStringValid(edtBirthday.getText().toString())) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date parse = sdf.parse(edtBirthday.getText().toString());
                c.setTime(parse);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.show();
    }


    @OnClick(R.id.btn_save_pf)
    public void onSaveClick() {
        if (StringUtil.checkStringValid(edtFullName.getText().toString().trim())) {
            params.put(APIConstant.FULL_NAME, edtFullName.getText().toString().trim());
            if (StringUtil.checkStringValid(edtAddress.getText().toString().trim())) {
                params.put(APIConstant.ADDRESS, edtAddress.getText().toString().trim());
            }
            if (StringUtil.checkStringValid(edtWeb.getText().toString().trim())) {
                params.put(APIConstant.WEB, edtWeb.getText().toString().trim());
            }
            if (StringUtil.checkStringValid(edtWork.getText().toString().trim())) {
                params.put(APIConstant.JOB, edtWork.getText().toString().trim());
            }
            if (StringUtil.checkStringValid(edtIntro.getText().toString().trim())) {
                params.put(APIConstant.DESCRIPTION, edtIntro.getText().toString().trim());
            }
            if (StringUtil.checkStringValid(edtBirthday.getText().toString().trim())) {
                params.put(APIConstant.BIRTHDAY, edtBirthday.getText().toString().trim());
            }
            if (imgAvatar != null) {
                fileMap.put(APIConstant.AVATAR_IMG, imgAvatar);
                sendRequestEditProfile();
            } else {
                sendRequestEditProfileWithOutImage();
            }
        } else {
            tvErrorName.setText(getString(R.string.edit_pf_name_have_regex));
            edtFullName.setBackground(getResources().getDrawable(R.drawable.et_name_edit_pf_error));
            UiUtil.showView(tvErrorName);
        }
    }

    private void sendRequestEditProfile() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UpdateProfileRequest request = new UpdateProfileRequest(params, fileMap);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    replaceFragment(R.id.container, ProfileFragment.newInstance(), true);
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

    private void sendRequestEditProfileWithOutImage() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UpdateProfileWithOutImage request = new UpdateProfileWithOutImage(params);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    replaceFragmentDontBack(R.id.container, ProfileFragment.newInstance());
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // 1=Male,0=Female
        if (position == FEMALE) {
            params.put(APIConstant.SEX, String.valueOf(MALE));
        } else if (position == MALE) {
            params.put(APIConstant.SEX, String.valueOf(FEMALE));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        params.put(APIConstant.SEX, String.valueOf(1));
    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri) {
        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            return filePath;
        }
        String imgId = m.group();

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{imgId}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }
}
