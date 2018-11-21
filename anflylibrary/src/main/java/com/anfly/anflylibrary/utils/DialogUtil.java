package com.anfly.anflylibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ai.yunji.delivery.MyConst;
import ai.yunji.delivery.R;
import ai.yunji.delivery.adapter.CancelTaskAdapter;
import ai.yunji.delivery.adapter.TaskArriveAdapter;
import ai.yunji.delivery.base.BaseMvpActivity;
import ai.yunji.delivery.bean.MainLayerBean;
import ai.yunji.delivery.bean.TaskBean;
import ai.yunji.delivery.bus.BusTaskChangeEvent;
import ai.yunji.delivery.mvpV.SettingActivity;
import ai.yunji.delivery.widget.FullScreenDialog;
import ai.yunji.delivery.widget.PasswordView;
import ai.yunji.running.RobotConnectAction;

public class DialogUtil {
    public static Dialog estopDialog(Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.fullrSceenMyDialog);
        dialog.setContentView(R.layout.dialog_estop);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog. getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    public static Dialog quCanDialog(Activity context, ArrayList<MainLayerBean> layerBeans, String district, View.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.fullrSceenMyDialog);
        dialog.setContentView(R.layout.dialog_task_arrive);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView confirm = dialog.findViewById(R.id.confirm_to_get);
        confirm.setOnClickListener(listener);
        RecyclerView rv_qucan = dialog.findViewById(R.id.rv_qucan);

        TaskArriveAdapter adapter = new TaskArriveAdapter(context);
        adapter.setChoosedDistrict(district);
        adapter.setData(layerBeans);
        rv_qucan.setLayoutManager(new LinearLayoutManager(context));
        rv_qucan.setAdapter(adapter);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static Dialog layerDialog(Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_plate_plies);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        RadioGroup radioGroup = dialog.findViewById(R.id.choose_plies);
        RadioButton four_plies = dialog.findViewById(R.id.four_plies);
        RadioButton three_plies = dialog.findViewById(R.id.three_plies);
        RadioButton two_plies = dialog.findViewById(R.id.two_plies);
        RadioButton one_plies = dialog.findViewById(R.id.one_plies);

        int layerNum = SharedPrefsUtil.get(MyConst.PLAT_LAYER, 4);
        switch (layerNum) {
            case 1:
                one_plies.setChecked(true);
                break;
            case 2:
                two_plies.setChecked(true);
                break;
            case 3:
                three_plies.setChecked(true);
                break;
            case 4:
                four_plies.setChecked(true);
                SharedPrefsUtil.put(MyConst.PLAT_LAYER, 4);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.four_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 4);
                        break;
                    case R.id.three_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 3);
                        break;
                    case R.id.two_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 2);
                        break;
                    case R.id.one_plies:
                        SharedPrefsUtil.put(MyConst.PLAT_LAYER, 1);
                        break;
                }
            }
        });
        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

//        int width = ActivityUtil.getScreenWidthMetrics(context) * 2 / 5;
//        int height = ActivityUtil.getScreenHeightMetrics(context) * 5 / 6;
//        LinearLayout ll = dialog.findViewById(R.id.ll_root);
//        ll.setLayoutParams(new LinearLayout.LayoutParams(width, height));


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }


    public static Dialog cancelTaskDialog(final Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_cancel_task);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        final CancelTaskAdapter adapter = new CancelTaskAdapter(context);

       /* ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
        out:
        for (int i = 0; i < MyConst.tasks.size(); i++) {
            in:
            for (TaskBean bean : tasks) {
                if (bean.getDistrict().equals( MyConst.tasks.get(i).getDistrict())) {
                    continue out;
                }
            }
            tasks.add(MyConst.tasks.get(i));
        }*/
//        adapter.setData(tasks);

        adapter.setData(MyConst.tasks);

        rv_list.setLayoutManager(new LinearLayoutManager(context));
        rv_list.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedNum = 0;
                List<TaskBean> data = adapter.getDataSource();
                for (int i = data.size() - 1; i >= 0; i--) {
                    if (data.get(i).isChecked()) {
                        selectedNum++;

                      /*  for (int k=MyConst.tasks.size()-1;k>=0;k--){
                            if (MyConst.tasks.get(k).getDistrict().equals(data.get(i).getDistrict())){
                                TaskBean district = MyConst.tasks.remove(k);
                                if (k == 0) {
                                    DeliApplication.deliApi.cancelTask(null);
                                    DeliApplication.deliApi.cancelTask(null);
                                    EventBus.getDefault().post(new BusTaskChangeEvent(district.getLayer(),k));
                                }else {
                                    EventBus.getDefault().post(new BusTaskChangeEvent(district.getLayer(),k));
                                }
                            }
                        }*/

                        TaskBean district = MyConst.tasks.remove(i);
                        if (i == 0) {
                            RobotConnectAction.init(null).moveCancel();
                            EventBus.getDefault().post(new BusTaskChangeEvent(district.getLayer(),i));
                        }else {
                            EventBus.getDefault().post(new BusTaskChangeEvent(district.getLayer(),i));
                        }
                    }
                }

                if (selectedNum > 0) {
                    dialog.dismiss();
                } else {
                    if (context instanceof BaseMvpActivity) {
                        ((BaseMvpActivity) context).showTs(context.getResources().getString(R.string.please_choose_task));
                    }
                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    public static Dialog voiceSettingDialog(final Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_voice_setting);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SeekBar seekBar = dialog.findViewById(R.id.seekBar);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        final AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);

        //获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }


    public static Dialog changePwdDialog(final BaseMvpActivity context) {
        final FullScreenDialog mModifyPasswordDialog = new FullScreenDialog(context);
//        final Dialog mModifyPasswordDialog=new Dialog(context,R.style.nomalDialog);

        LayoutInflater inflater = context.getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_modify_password, null);
//        int width = ActivityUtil.getScreenWidthMetrics(context);
//        int height =  ActivityUtil.getScreenHeightMetrics(context);
//        mModifyPasswordDialog.setWidthAndHeight(width, height);
        final EditText mEtPasswordNew = contentView.findViewById(R.id.et_password_new);
        final EditText mEtPasswordConfirm = contentView.findViewById(R.id.et_password_confirm);
        Button mButtonConfirm = contentView.findViewById(R.id.button_confirm);
        Button mButtonCancel = contentView.findViewById(R.id.button_cancel);
        if (mModifyPasswordDialog != null && !mModifyPasswordDialog.isShowing()) {
            mModifyPasswordDialog.show();
        }
        /*键盘*/
        final NumKeyBoardUtil keyboardUtilNew = new NumKeyBoardUtil(contentView, context, mEtPasswordNew, "");
        keyboardUtilNew.showKeyboard();
        mModifyPasswordDialog.setContentView(contentView);
        mModifyPasswordDialog.setCanceledOnTouchOutside(true);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEtPasswordNew.getText().toString()) || TextUtils.isEmpty(mEtPasswordConfirm.getText().toString())) {
                    context.showTs(context.getResources().getString(R.string.pwd_cant_null));
                    return;
                }
                int lengthPasswordNew = mEtPasswordNew.getText().length();
                int lengthPasswordConfirm = mEtPasswordConfirm.getText().length();
                if (lengthPasswordNew != 6 || lengthPasswordConfirm != 6) {
                    context.showTs(context.getResources().getString(R.string.pwd_min_6));
                    return;
                }
                if (!mEtPasswordNew.getText().toString().equals(mEtPasswordConfirm.getText().toString())) {
                    context.showTs(context.getResources().getString(R.string.pwd_not_equal));
                    return;
                }
                SharedPrefsUtil.put(MyConst.SHARE_KEY_PWD, mEtPasswordConfirm.getText().toString());
                context.showTs(context.getResources().getString(R.string.pwd_change_success));
                if (mModifyPasswordDialog != null && mModifyPasswordDialog.isShowing()) {
                    mModifyPasswordDialog.dismiss();
                }
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyPasswordDialog != null && mModifyPasswordDialog.isShowing()) {
                    mModifyPasswordDialog.dismiss();
                }
            }
        });

        /*输入密码*/
        mEtPasswordNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtilNew.showKeyboard();
                keyboardUtilNew.setEditText(mEtPasswordNew);
                mEtPasswordNew.requestFocus();
                return false;
            }
        });

        /*确认密码*/
        mEtPasswordConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtilNew.showKeyboard();
                keyboardUtilNew.setEditText(mEtPasswordConfirm);
                mEtPasswordConfirm.requestFocus();
                return false;
            }
        });


        return mModifyPasswordDialog;

    }

    public static Dialog inputPwdDialog(final BaseMvpActivity context) {
//        final FullScreenDialog mPasswordDialog=new FullScreenDialog(context);
        final Dialog mPasswordDialog = new Dialog(context, R.style.nomalDialog);
        LayoutInflater inflater = context.getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_password, null);
//        int width = ActivityUtil.getScreenWidthMetrics(context);
//        int height =  ActivityUtil.getScreenHeightMetrics(context);
//        mPasswordDialog.setWidthAndHeight(width, height);
        final TextView mPasswordTitle = (TextView) contentView.findViewById(R.id.password_title);
        final PasswordView passwordView = (PasswordView) contentView.findViewById(R.id.password_view);
        passwordView.setInputType(InputType.TYPE_NULL);
        final NumKeyBoardUtil keyboardUtil = new NumKeyBoardUtil(contentView, context, passwordView);
        passwordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.showKeyboard();
                return false;
            }
        });

        if (mPasswordDialog != null && !mPasswordDialog.isShowing()) {
//            mPasswordDialog.setCanceledOnTouchOutside(true);
//            mPasswordDialog.setCancelable(true);
            mPasswordDialog.show();
        }

        /*输入密码自动弹出键盘*/
        if (keyboardUtil != null) {
            keyboardUtil.showKeyboard();
        }

        mPasswordDialog.setContentView(contentView);
        passwordView.setOnFinishListener(new PasswordView.OnFinishListener() {
            @Override
            public void setOnPasswordFinished(String text) {
                String password = SharedPrefsUtil.get(MyConst.SHARE_KEY_PWD, "000000");
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() == 6) {
                        if (text.equals(password) || text.equals("789789")) {
                            context.startActivity(new Intent(context, SettingActivity.class));
                            mPasswordDialog.dismiss();
                        } else {//密码错误
                            mPasswordTitle.setText("密码错误");
                            mPasswordTitle.setTextColor(Color.parseColor("#dc4c2d"));
                        }
                    } else {
                        mPasswordTitle.setText("请输入6位密码");
                        mPasswordTitle.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }
        });


        keyboardUtil.setOncompleteListener(new PasswordView.OnFinishListener() {
            @Override
            public void setOnPasswordFinished(String text) {
                String password = SharedPrefsUtil.get(MyConst.SHARE_KEY_PWD, "000000");
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() == 6) {
                        if (text.equals(password) || text.equals("789789")) {
                            context.startActivity(new Intent(context, SettingActivity.class));
                            mPasswordDialog.dismiss();
                        } else {//密码错误
                            mPasswordTitle.setText("密码错误");
                            mPasswordTitle.setTextColor(Color.parseColor("#dc4c2d"));
                        }
                    } else {
                        mPasswordTitle.setText("请输入6位密码");
                        mPasswordTitle.setTextColor(Color.parseColor("#dc4c2d"));
                    }
                }
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardUtil.showKeyboard();
            }
        });
/*        mPasswordDialog.setOnTouchListener(new FullScreenDialog.onTouchListener() {
            @Override
            public void onDialogTouch(MotionEvent event) {
                keyboardUtil.showKeyboard();
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//                        if (keyboardUtil != null) {
//                            keyboardUtil.hideKeyboard();
//                        }
//                    }
//                }
            }
        });*/


        mPasswordDialog.setCanceledOnTouchOutside(true);
        mPasswordDialog.setCancelable(true);


        return mPasswordDialog;

    }

    public static Dialog updateDialog(final Activity context, String versionName, View.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SeekBar seekBar = dialog.findViewById(R.id.seekBar);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(listener);

        TextView tv_info = dialog.findViewById(R.id.tv_info);
        tv_info.setText(versionName);


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

/*
    public static Dialog downLoadDialog(final Activity context) {
        if (context == null) {
            return null;
        }
        if (context.isFinishing()) {
            return null;
        }
        final Dialog dialog = new Dialog(context, R.style.nomalDialog);
        dialog.setContentView(R.layout.dialog_download_progress);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;


    }*/
}
