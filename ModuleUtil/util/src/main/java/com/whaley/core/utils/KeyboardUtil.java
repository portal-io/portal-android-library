package com.whaley.core.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by YangZhi on 2016/12/9 16:00.
 */

public class KeyboardUtil {

    public static void showKeyBoard(final EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyBoard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
