package im.hua.artofandroid.chapter_12_1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import im.hua.artofandroid.R;

public class BitmapFactoryActivity extends AppCompatActivity {

    public static final int DISK_CACHE_INDEX = 0;
    public static final String IMAGE_URL = "http://ww4.sinaimg.cn/mw1024/5033b6dbjw1f9agnbqg5qj20qo0qogt1.jpg";
    private DiskLruCache mDiskLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_factory);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.pic_beauty, imageView.getWidth(), imageView.getHeight()));
            }
        });

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        try {
            mDiskLruCache = DiskLruCache.open(file, 1, 1, 1024 * 1024 * 50);
            downUrlToStream();

            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyFormUrl(IMAGE_URL));
            InputStream inputStream = null;
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(0);
                imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            byte[] result = messageDigest.digest();
            cacheKey = bytesToHexString(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    public Bitmap decodeSampledBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        int width = options.outWidth;
        int height = options.outHeight;

        Log.d("BitmapFactoryActivity", width + ":" + height);

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            inSampleSize *= 2;
            while (width / inSampleSize >= reqWidth || height / inSampleSize >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
        Log.d("BitmapFactoryActivity", "result:" + bitmap.getWidth() + ":" + bitmap.getHeight());
        return bitmap;
    }

    public void downUrlToStream() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DiskLruCache.Editor edit = null;
                OutputStream outputStream = null;
                String key = hashKeyFormUrl(IMAGE_URL);

                try {
                    edit = mDiskLruCache.edit(key);
                    if (null != edit) {
                        outputStream = edit.newOutputStream(DISK_CACHE_INDEX);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HttpURLConnection httpURLConnection = null;

                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                try {
                    URL url = new URL("http://ww4.sinaimg.cn/mw1024/5033b6dbjw1f9agnbqg5qj20qo0qogt1.jpg");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    in = new BufferedInputStream(httpURLConnection.getInputStream(), 1024);
                    out = new BufferedOutputStream(outputStream, 1024);
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    edit.commit();
                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        edit.abort();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        try {
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
}
