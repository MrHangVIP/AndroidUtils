package Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.util.List;

/**
 * Created by moram on 2016/9/7.
 */
public class MyUtil {

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 跳转至拨号页面
     *
     * @param context
     * @param phoneNumber
     */
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));

    }


    /**
     * 发送短信
     *
     * @param context
     * @param phoneNumber
     * @param message
     */
    public static void sendSms(Context context, String phoneNumber, String message) {
        Uri uri = Uri.parse("smsto." + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }

    /**
     * 唤醒屏幕并解锁
     *
     * @param context
     */
    public static void wakeUpandUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        wl.release();
    }

    /**
     * 判断当前应用是否在最前端
     *
     * @param context
     * @return
     */
    public static boolean isAppBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(1);
        if (!taskInfoList.isEmpty()) {
            ComponentName topActivity = taskInfoList.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断手机是否处于锁屏（睡眠）
     *
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = km.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 判断手机是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断当前设备是不是手机
     *
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (phoneManager != null && phoneManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        }
        return true;
    }


    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }


    /**
     * 获取设备IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context) {
        String deviceID;
        if (isPhone(context)) {
            TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceID = phoneManager.getDeviceId();
        } else {
            deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceID;
    }

    /**
     * 获取设备mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            return macAddress = wifiManager.getConnectionInfo().getMacAddress();
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        new Thread(new Runnable() {//此时最好通过回调返回因为开启wifi需要时间
            @Override
            public void run() {

            }
        }).start();

        return macAddress = wifiManager.getConnectionInfo().getMacAddress();
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 判断是否存在sd卡
     *
     * @param context
     * @return
     */
    public static boolean haveSDCard(Context context) {

        return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 动态显示软键盘
     *
     * @param context
     * @param edit
     */
    public static void showSoftInput(Context context, EditText edit) {

        edit.setFocusable(true);

        edit.setFocusableInTouchMode(true);

        edit.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(edit, 0);

    }

    /**
     * 跳转home页面
     *
     * @param context
     */
    public static void goHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {

        Rect frame = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        return frame.top;

    }


    /**
     * 获取状态栏高度＋标题栏(ActionBar)高度
     * (注意，如果没有ActionBar，那么获取的高度将和上面的是一样的，只有状态栏的高度)
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {

        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

    }


    /**
     * 获取MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)
     * 仅当用户已在网络注册时有效, CDMA 可能会无效（中国移动：46000 46002, 中国联通：46001,中国电信：46003）
     *
     * @param context
     * @return
     */
    public static String getNetworkOperator(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getNetworkOperator();

    }


    /**
     * 返回移动网络运营商的名字
     * (例：中国联通、中国移动、中国电信) 仅当用户已在网络注册时有效, CDMA 可能会无效)
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getNetworkOperatorName();

    }


    /**
     * @param context
     * @return 返回移动终端类型
     * PHONE_TYPE_NONE :0 手机制式未知
     * PHONE_TYPE_GSM :1 手机制式为GSM，移动和联通
     * PHONE_TYPE_CDMA :2 手机制式为CDMA，电信
     * PHONE_TYPE_SIP:3
     */
    public static int getPhoneType(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getPhoneType();

    }


    public class Constants {

        /**
         * Unknown network class
         */

        public static final int NETWORK_CLASS_UNKNOWN = 0;

        /**
         * wifi net work
         */

        public static final int NETWORK_WIFI = 1;

        /**
         * "2G" networks
         */

        public static final int NETWORK_CLASS_2_G = 2;

        /**
         * "3G" networks
         */

        public static final int NETWORK_CLASS_3_G = 3;

        /**
         * "4G" networks
         */

        public static final int NETWORK_CLASS_4_G = 4;

    }

    /**
     * 判断手机连接的网络类型(2G,3G,4G)
     * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     */
    public static int getNetWorkClass(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {

            case TelephonyManager.NETWORK_TYPE_GPRS:

            case TelephonyManager.NETWORK_TYPE_EDGE:

            case TelephonyManager.NETWORK_TYPE_CDMA:

            case TelephonyManager.NETWORK_TYPE_1xRTT:

            case TelephonyManager.NETWORK_TYPE_IDEN:

                return Constants.NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:

            case TelephonyManager.NETWORK_TYPE_EVDO_0:

            case TelephonyManager.NETWORK_TYPE_EVDO_A:

            case TelephonyManager.NETWORK_TYPE_HSDPA:

            case TelephonyManager.NETWORK_TYPE_HSUPA:

            case TelephonyManager.NETWORK_TYPE_HSPA:

            case TelephonyManager.NETWORK_TYPE_EVDO_B:

            case TelephonyManager.NETWORK_TYPE_EHRPD:

            case TelephonyManager.NETWORK_TYPE_HSPAP:

                return Constants.NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:

                return Constants.NETWORK_CLASS_4_G;

            default:

                return Constants.NETWORK_CLASS_UNKNOWN;

        }

    }


    /**
     * 判断当前手机的网络类型(WIFI还是2,3,4G)
     * 需要用到上面的方法
     *
     * @param context
     * @return
     */
    public static int getNetWorkStatus(Context context) {

        int netWorkType = Constants.NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {

                netWorkType = Constants.NETWORK_WIFI;

            } else if (type == ConnectivityManager.TYPE_MOBILE) {

                netWorkType = getNetWorkClass(context);

            }

        }

        return netWorkType;

    }


    /**
     * dp转换px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }

    /**
     * px转换dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5);

    }


    /**
     * px转换sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / fontScale + 0.5);

    }

    /**
     * sp转换px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5);

    }


    /**
     * 把一个毫秒数转化成时间字符串
     * <p/>
     * 格式为小时/分/秒/毫秒（如：24903600 –> 06小时55分03秒600毫秒）
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒600毫秒）。
     */

    public static String millisToString(long millis, boolean isWhole,

                                        boolean isFormat) {

        String h = "";

        String m = "";

        String s = "";

        String mi = "";

        if (isWhole) {

            h = isFormat ? "00小时" : "0小时";

            m = isFormat ? "00分" : "0分";

            s = isFormat ? "00秒" : "0秒";

            mi = isFormat ? "00毫秒" : "0毫秒";

        }

        long temp = millis;

        long hper = 60 * 60 * 1000;

        long mper = 60 * 1000;

        long sper = 1000;

        if (temp / hper > 0) {

            if (isFormat) {

                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";

            } else {

                h = temp / hper + "";

            }

            h += "小时";

        }

        temp = temp % hper;

        if (temp / mper > 0) {

            if (isFormat) {

                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";

            } else {

                m = temp / mper + "";

            }

            m += "分";

        }

        temp = temp % mper;

        if (temp / sper > 0) {

            if (isFormat) {

                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";

            } else {

                s = temp / sper + "";

            }

            s += "秒";

        }

        temp = temp % sper;

        mi = temp + "";

        if (isFormat) {

            if (temp < 100 && temp >= 10) {

                mi = "0" + temp;

            }

            if (temp < 10) {

                mi = "00" + temp;

            }

        }

        mi += "毫秒";

        return h + m + s + mi;

    }


    /**
     * 格式为小时/分/秒/毫秒（如：24903600 –> 06小时55分03秒）。
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒）。
     */

    public static String millisToStringMiddle(long millis, boolean isWhole,

                                              boolean isFormat) {

        return millisToStringMiddle(millis, isWhole, isFormat, "小时", "分钟", "秒");

    }

    public static String millisToStringMiddle(long millis, boolean isWhole,

                                              boolean isFormat, String hUnit, String mUnit, String sUnit) {

        String h = "";

        String m = "";

        String s = "";

        if (isWhole) {

            h = isFormat ? "00" + hUnit : "0" + hUnit;

            m = isFormat ? "00" + mUnit : "0" + mUnit;

            s = isFormat ? "00" + sUnit : "0" + sUnit;

        }

        long temp = millis;

        long hper = 60 * 60 * 1000;

        long mper = 60 * 1000;

        long sper = 1000;

        if (temp / hper > 0) {

            if (isFormat) {

                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";

            } else {

                h = temp / hper + "";

            }
        }
        return h + m + s;

    }
}
