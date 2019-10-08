package com.ericwyn.quickopen;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {

    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setTitle("请选择想要启动的应用");

        setTitle("选择要启动的应用");
        sharedPreferences = getSharedPreferences("QuickOpen", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_launcher);
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView pgNameTextView = (TextView) view.findViewById(R.id.tv_app_pg_ame);
                TextView appNameTextView = (TextView) view.findViewById(R.id.tv_app_name);

                editor.putString("appName", pgNameTextView.getText().toString());
                editor.apply();

                if (toast == null){
                    toast = Toast.makeText(LauncherActivity.this, "启动应用设置为: " + appNameTextView.getText().toString(), Toast.LENGTH_LONG);
                } else {
                    toast.cancel();
                    toast = Toast.makeText(LauncherActivity.this, "启动应用设置为: " + appNameTextView.getText().toString(), Toast.LENGTH_LONG);
//                    toast.setText("启动应用设置为:" + appNameTextView.getText());
//                    toast.setDuration(Toast.LENGTH_LONG);
                }
                toast.show();

            }
        });
        initAppList();

    }

    public static List<MyAppInfo> mLocalInstallApps = null;

    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
                MyAppInfo myAppInfo = new MyAppInfo();
//                myAppInfo.setAppName(packageInfo.packageName);
//                ApplicationInfo appInfo = ctx.getApplicationInfo();
//                appName = (String) packManager.getApplicationLabel(appInfo);
                myAppInfo.setPackageName(packageInfo.packageName);
                myAppInfo.setAppName((String) packageManager.getApplicationLabel(packageInfo.applicationInfo));
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e("Scan Package","===============获取应用包信息失败");
        }
        return myAppInfos;
    }

    private void initAppList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<MyAppInfo> appInfos = scanLocalInstallAppList(LauncherActivity.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }


    class AppAdapter extends BaseAdapter {

        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();

        public void setData(List<MyAppInfo> myAppInfos) {
            this.myAppInfos = myAppInfos;
            notifyDataSetChanged();
        }

        public List<MyAppInfo> getData() {
            return myAppInfos;
        }

        @Override
        public int getCount() {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            MyAppInfo myAppInfo = myAppInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                mViewHolder.tx_app_pk_name = (TextView)convertView.findViewById(R.id.tv_app_pg_ame);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
            mViewHolder.tx_app_pk_name.setText(myAppInfo.getPackageName());
            return convertView;
        }

        class ViewHolder {

            ImageView iv_app_icon;
            TextView tx_app_name;
            TextView tx_app_pk_name;
        }
    }


}
