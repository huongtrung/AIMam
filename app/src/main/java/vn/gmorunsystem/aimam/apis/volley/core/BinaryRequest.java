package vn.gmorunsystem.aimam.apis.volley.core;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class BinaryRequest<T> extends GsonRequest<T> {
    private String mFilePath;
    private String mBody;
    private String bodyContentType = "application/json";

    public BinaryRequest(int method, String url, Response.ErrorListener listener, Class<T> mClass, Map<String, String> mHeader, Response.Listener<T> mListener, String mFilePath, String mBody) {
        super(method, url, mClass, mHeader, null, mListener, listener);
        this.mFilePath = mFilePath;
        this.mBody = mBody;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = new byte[0];
        try {
            if (!TextUtils.isEmpty(mBody)) {
                body = mBody.getBytes(getParamsEncoding());
                bodyContentType = "application/json";
            } else {
                body = fullyReadFileToBytes(mFilePath);
                bodyContentType = "image/jpeg";
            }
            String entityContentAsString = new String(body);
            Log.e("volley", entityContentAsString);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return body;
    }

    private byte[] fullyReadFileToBytes(String path) throws IOException {
        File f = new File(path);
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        ;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    @Override
    public String getBodyContentType() {
        return bodyContentType;
    }

}
