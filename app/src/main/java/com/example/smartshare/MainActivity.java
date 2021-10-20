package com.example.smartshare;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static List<MyApps> appsList=new ArrayList<>();
    public static ImageView shareButton;
    public static void eraseAll(View v)
    {  int len=appsList.size();
       for(int i=0;i<len;i++)
       {
          View view=appsList.get(i).getApps_view();
          if(view!=v) {
              RadioButton radioButton = view.findViewById(R.id.select);
              if(radioButton.isChecked())
              {   appsList.get(i).setA(0);
                  radioButton.setChecked(false);
              }
          }
       }

    }
    public static void FBHide()
    {
        shareButton.setVisibility(View.GONE);
    }
    public static void FBShow()
    {
        shareButton.setVisibility(View.VISIBLE);
    }


    public void shareApp(int i)
    {    String filepath=appsList.get(i).getApplicationInfo().sourceDir;
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");
        File originalApk=new File(filepath);
        try{
            File tempFile=new File(this.getExternalCacheDir()+"/ExtractedApk");
            if(!tempFile.isDirectory())
                if(!tempFile.mkdirs())
                    return;
                if(appsList.get(i).getName().equalsIgnoreCase("SmartShare"))
                    tempFile=new File(tempFile.getPath()+"/"+appsList.get(i).getName().replace(" ","")+".apk");
                else
                    tempFile=new File(tempFile.getPath()+"/"+appsList.get(i).getName().replace(" ","")+"_from_SmartShare"+".apk");
            if(!tempFile.exists())
                if(!tempFile.createNewFile())
                    return;
           //just to check app is installed or get unistalled
            try{ InputStream input=new FileInputStream(originalApk);}
            catch (Exception e){
                Toast.makeText(this,"App doesn't exist",Toast.LENGTH_SHORT).show();}
            InputStream in=new FileInputStream(originalApk);
            OutputStream out=new FileOutputStream(tempFile);

            byte[] buf=new byte[1024];
            int len;
            while((len=in.read(buf))>0)
                out.write(buf,0,len);
            in.close();
            out.close();
            Uri photoURI= FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID+".provider",tempFile);
            intent.putExtra(Intent.EXTRA_STREAM,photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try{
                startActivity(Intent.createChooser(intent,"Share "+appsList.get(i).getName()+" app via"));}
            catch (Exception e ) {
                Toast.makeText(MainActivity.this, "Sorry,app sharing is not supported", Toast.LENGTH_SHORT).show();
            }   } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.refreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                ListApps();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        shareButton=findViewById(R.id.send);
        shareButton.setVisibility(View.GONE);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                for( i=0;i<appsList.size();i++) {
                    RadioButton r=appsList.get(i).getApps_view().findViewById(R.id.select);
                    if(r.isChecked())
                        break;
                }
                shareApp(i);

            }
        });

        //   PackageManager pm  = getApplicationContext().getPackageManager();
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

//        final PackageManager pm = getActivity().getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo: packages){
            String name="";
            //To filter out system apps
            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                continue;
            }
            if((name = String.valueOf(pm.getApplicationLabel(packageInfo))).isEmpty()){
                name = packageInfo.packageName;
            }
            Drawable icon=pm.getApplicationIcon(packageInfo);
            String apkPath = packageInfo.sourceDir;
            long apkSize = new File(packageInfo.sourceDir).length();
            MyApps app=new MyApps(this,name,icon,apkPath,apkSize,packageInfo);
            appsList.add(app);
        }
        Collections.sort(appsList, new Comparator<MyApps>() {
                    @Override
                    public int compare(MyApps app1,MyApps app2) {
                        return app1.getName().toLowerCase().compareTo(app2.getName().toLowerCase());
                    }
                }
        );
        LinearLayout LLV=findViewById(R.id.LLV);
        //LLV.setGravity(Gravity.CENTER_HORIZONTAL);
        int len=appsList.size();
        outer: for(int i=0;i<len;i++)
        {   LinearLayout LLH=new LinearLayout(MainActivity.this);
            LLH.setOrientation(LinearLayout.HORIZONTAL);
            LLH.setGravity(Gravity.CENTER|Gravity.TOP);
            LLV.addView(LLH);

                TextView tv=new TextView(MainActivity.this);
                tv.setText(" ");
                tv.setHeight(5);
                LLV.addView(tv);

            for(int j=0;j<4;j++)
                if(i+j<len)
                { LLH.addView(appsList.get(i+j).getApps_view());
                }
                else break outer;
            i+=3;
        }
        TextView tv=new TextView(MainActivity.this);
        tv.setText(" ");
        tv.setHeight(180);
        LLV.addView(tv);


    }

}