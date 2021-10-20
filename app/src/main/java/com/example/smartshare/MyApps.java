package com.example.smartshare;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.smartshare.MainActivity.eraseAll;
import static com.example.smartshare.MainActivity.FBHide;
import static com.example.smartshare.MainActivity.FBShow;
public class MyApps  extends AppCompatActivity{
    private String name;
    private Drawable icon;
    private String apkPath;
    private long apkSize;
    private ConstraintLayout Apps_view;
    private View vi;

    @Override
    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    ApplicationInfo applicationInfo;
    final int []a;
    public void setA(int a) {
        this.a[0]= a;
    }

    public ConstraintLayout getApps_view() {
        return Apps_view;
    }


    public MyApps(Context context,String name, Drawable icon, String apkPath, long apkSize,ApplicationInfo appInfo) {
        this.name = name;
        this.icon = icon;
        this.apkPath = apkPath;
        this.apkSize = apkSize;
        this.applicationInfo=appInfo;
//        LayoutInflater inflater;
//        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v= inflater.inflate( R.layout.apps_layout,null);
         vi=LayoutInflater.from(context).inflate(R.layout.apps_layout,null);
        TextView app_name=vi.findViewById(R.id.app_name);
        app_name.setText(name);
        TextView app_size=vi.findViewById(R.id.app_size);
        app_size.setText(twoDecimal(apkSize));
        app_name.setText(name);
        ImageView iconImg=vi.findViewById(R.id.app_icon);
        iconImg.setImageDrawable(icon);
        RadioButton radioButton=vi.findViewById(R.id.select);
        LinearLayout linearLayout=vi.findViewById(R.id.LL);
        a=new int[1];
        a[0]=0;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getSelect(radioButton);

            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelect(radioButton);
            }
        });

        Apps_view = (ConstraintLayout) vi;
    }
    void getSelect(RadioButton radioButton)
    {
        if(a[0] ==0) {
            FBShow();
            eraseAll(vi);
            radioButton.setChecked(true);

        }
        else {
            FBHide();
            radioButton.setChecked(false);
        }
        a[0] =1- a[0];
    }




    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
    public String getApkPath() {
        return apkPath;
    }

    public long getApkSize() {
        return apkSize;
    }
    private String twoDecimal(long Size) {

        String size;
        if(Size<1024){
            size = String.format("%1$.2f B",(double) Size);
        } else if(Size < Math.pow(1024,2)){
            size = String.format("%1$.2f KB",(double)(Size/1024));
        }else if(Size < Math.pow(1024,3)){
            size = String.format("%1$.2f MB",(double)(Size/Math.pow(1024,2)));
        } else{
            size = String.format("%1$.2f GB",(double) (Size/Math.pow(1024,3)));
        }
        return size;
    }
}


