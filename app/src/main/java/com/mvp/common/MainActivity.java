package com.mvp.common;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hmin66.common.R;
import com.hmin66.commonlibrary.dialog.BaseDialog;
import com.hmin66.commonlibrary.dialog.DialogChain;
import com.hmin66.commonlibrary.dialog.DialogInterceptor;
import com.hmin66.commonlibrary.utils.DisplayUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.show_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChain dialogChain = DialogChain.create(3)
                        .attach(MainActivity.this)
                        .addInterceptor(initDialog("A弹窗", true))
                        .addInterceptor(initDialog("B弹窗", false))
                        .addInterceptor(initDialog("C弹窗", true))
                        .build();
                dialogChain.process();
            }
        });
    }

    public BaseDialog initDialog(String tips, final boolean isShow){
        final BaseDialog dialog = new BaseDialog.Builder(this)
                .setContentView(R.layout.dialog_chain)
                .setWidthAndHeight(DisplayUtil.dipTopx(this, 295), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setInterceptor(new DialogInterceptor(){
                    @Override
                    public boolean intercept(DialogChain chain) {
                        return !isShow;
                    }
                })
                .create();

        ((TextView)dialog.getView(R.id.dialog_tips_tv)).setText(tips);

        dialog.getView(R.id.dialog_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getView(R.id.dialog_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("HMin66", "onDismiss1: ");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("HMin66", "onDismiss2: ");
            }
        });

        return dialog;
    }
}
