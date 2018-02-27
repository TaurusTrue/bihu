package com.example.taurus.bihu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.example.taurus.bihu.utils.Response;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.logging.LogRecord;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Taurus on 2018/2/21.
 * 网络请求工具类
 */

public class HttpUtil {

    public static void sendHttpRequest(final String address, final String param, final HttpCallbackListener listener) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(param.getBytes());
                    os.flush();
                    os.close();
                    if (listener != null) {
                        final byte[] temp = read(connection.getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFinish(new Response(temp));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        //回调onError方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestV(final String address, final String param, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(param.getBytes());
                    os.flush();
                    os.close();
                    if (listener != null) {
                        final byte[] temp = read(connection.getInputStream());
                        listener.onFinish(new Response(temp));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        //回调onError方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestH(final String address, final String param, final HttpCallbackListener listener) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(param.getBytes());
                    os.flush();
                    os.close();
                    if (listener != null) {
                        final byte[] temp = read(connection.getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFinish(new Response(temp,0));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        //回调onError方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public interface HttpCallbackListener {
        void onFinish(Response response);

        void onError(Exception e);
    }

    private static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len;
        while ((len = is.read(temp)) != -1)
            outputStream.write(temp, 0, len);
        is.close();
        return outputStream.toByteArray();
    }

    public static void loadImageResource(final String imageUrl, final loadBitmap loadbitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                Bitmap bitmap = null;
                try {
                    url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    loadbitmap.onFinish(bitmap);
                } catch (Exception e) {
                    Log.d("TAG", e.toString());
                    loadbitmap.onError(e);
                }
            }
        }).start();
    }

    public static void loadAvatar(final String imageUrl, final ImageView avatar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                Bitmap bitmap = null;
                try {
                    url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    final Bitmap finalBitmap = bitmap;
                    avatar.post(new Runnable() {
                        @Override
                        public void run() {
                            avatar.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (Exception e) {
                    Log.d("TAG", e.toString());
                }
            }
        }).start();
    }

    public interface loadBitmap {
        void onFinish(Bitmap bitmap);

        void onError(Exception e);
    }

}
