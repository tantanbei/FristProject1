package com.whoplate.paipable.viewHolder;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivitySignIn;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.OkPacket;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Response;

public class ExchangeListViewHolder extends RecyclerView.ViewHolder {
    final WeakReference<XActivity> a;

    public TextView title;
    public TextView point;
    public TextView submit;
    public ImageView cover;

    public String value;
    public int productId;

    AlertDialog dialog;

    public ExchangeListViewHolder(View itemView, XActivity realA) {
        super(itemView);
        this.a = new WeakReference<XActivity>(realA);

        title = (TextView) itemView.findViewById(R.id.title);
        point = (TextView) itemView.findViewById(R.id.point);
        submit = (TextView) itemView.findViewById(R.id.submit);
        cover = (ImageView) itemView.findViewById(R.id.cover);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(a.get());
                    builder.setTitle("提示");
                    builder.setMessage(String.format("为您注册的手机充值%s元?", value));
                    builder.setIcon(R.mipmap.logo);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //关闭dialog
                            XThread.RunBackground(new Runnable() {
                                @Override
                                public void run() {
                                    DoExchange();
                                }
                            });
                        }
                    });

                    dialog = builder.create();
                }
                dialog.show();
            }
        });
    }

    private void DoExchange() {
        try {

            Response response = Http.Get(Const.URL_API + Const.URL_EXCHANGE + "?productid=" + productId);
            final OkPacket packet = LoganSquare.parse(response.body().byteStream(), OkPacket.class);

            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!packet.Ok) {
                        switch (packet.Data) {
                            case "0":
                                XToast.Show("积分不足");
                                break;
                            default:
                                XToast.Show("兑换失败");
                        }
                    } else {
                        XToast.Show("兑换成功");

                        if (a.get() instanceof ActivitySignIn) {
                            ((ActivitySignIn) a.get()).RefreshData();
                        }
                    }
                }
            });
        } catch (IOException e) {
            XToast.Show(R.string.request_fails);
            XDebug.Handle(e);
        }
    }
}
