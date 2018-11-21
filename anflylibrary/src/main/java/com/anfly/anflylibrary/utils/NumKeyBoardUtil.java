package com.anfly.anflylibrary.utils;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import ai.yunji.delivery.R;
import ai.yunji.delivery.widget.PasswordView;

/**
 * 数字软键盘工具类
 */
public class NumKeyBoardUtil {
    private KeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private PasswordView ed;
    private EditText editText;

    public NumKeyBoardUtil(View view, Context ctx, PasswordView edit) {
        this.ed = edit;
        k = new Keyboard(ctx, R.xml.number);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public NumKeyBoardUtil(View view, Context ctx, EditText editText, String string) {
        this.editText = editText;
        k = new Keyboard(ctx, R.xml.number);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (ed != null) {
                Editable editable = ed.getText();
                int start = ed.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                    onCompletClick();
                    hideKeyboard();
                } else { //将要输入的数字现在编辑框中
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
            if (editText != null) {
                Editable editable = editText.getText();
                int start = editText.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                    onCompletClick();
                    hideKeyboard();
                } else { //将要输入的数字现在编辑框中
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }

        }
    };

    public void showKeyboard() {
        keyboardView.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        keyboardView.setVisibility(View.GONE);
    }

    public int getKeyboardVisible() {
        return keyboardView.getVisibility();
    }




    PasswordView.OnFinishListener onClickListener;
    public void setOncompleteListener(PasswordView.OnFinishListener listener){
        this.onClickListener=listener;
    }
    private void onCompletClick(){
        if (onClickListener!=null){
            String str=ed.getText().toString();
            onClickListener.setOnPasswordFinished(str);
        }
    }
}
