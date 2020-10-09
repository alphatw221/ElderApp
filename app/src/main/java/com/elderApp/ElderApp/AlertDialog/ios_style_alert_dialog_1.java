package com.elderApp.ElderApp.AlertDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elderApp.ElderApp.R;

public class ios_style_alert_dialog_1 extends Dialog {

    public ios_style_alert_dialog_1(Context context) {
        super(context);
    }

    public static class Builder {
        private Context mContext;
        private ios_style_alert_dialog_1 mIosDialog;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private View mContentView;
        private OnClickListener mPositiveButtonClickListener;
        private OnClickListener mNegativeButtonClickListener;
        private boolean mCancelable = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(int titleId) {
            this.mTitle = mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(int messageId) {
            this.mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder setPositiveButton(int textId, OnClickListener listener) {
            this.mPositiveButtonText = mContext.getText(textId);
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnClickListener listener) {
            this.mNegativeButtonText = mContext.getText(textId);
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.mNegativeButtonText = text;
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        public ios_style_alert_dialog_1 create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.alert_dialog_ios_style_1, null);
            mIosDialog = new ios_style_alert_dialog_1(mContext);
            mIosDialog.setCancelable(mCancelable);

            mIosDialog.setContentView(dialogView);

            TextView tvTitle = (TextView) dialogView.findViewById(R.id._ios_style_alert_dialog1_title);
            TextView tvMessage = (TextView) dialogView.findViewById(R.id._ios_style_alert_dialog1_text);
            Button btnCancel = (Button) dialogView.findViewById(R.id._ios_style_alert_dialog1_cancel);
            Button btnConfirm = (Button) dialogView.findViewById(R.id._ios_style_alert_dialog1_ok);
            View horizontal_line = dialogView.findViewById(R.id._ios_style_alert_dialog1_hline);
            View vertical_line = dialogView.findViewById(R.id._ios_style_alert_dialog1_vline);
            View btns_panel = dialogView.findViewById(R.id._ios_style_alert_dialog1_btn_layout);

            // set title
            // fix #1,if title is null,set title visibility GONE
            if(TextUtils.isEmpty(mTitle)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(mTitle);
            }
            tvMessage.setText(mMessage);
            // set buttons
            if(mPositiveButtonText == null && mNegativeButtonText == null) {
                setPositiveButton("確認", null);
//                btnConfirm.setBackgroundResource(R.drawable.iosdialog_sigle_btn_selector);
                btnCancel.setVisibility(View.GONE);
                vertical_line.setVisibility(View.GONE);
            } else if(mPositiveButtonText != null && mNegativeButtonText == null) {
//                btnConfirm.setBackgroundResource(R.drawable.iosdialog_sigle_btn_selector);
                btnCancel.setVisibility(View.GONE);
                vertical_line.setVisibility(View.GONE);
            } else if(mPositiveButtonText == null && mNegativeButtonText != null) {
                btnConfirm.setVisibility(View.GONE);
//                btnCancel.setBackgroundResource(R.drawable.iosdialog_sigle_btn_selector);
                vertical_line.setVisibility(View.GONE);
            }
            if (mPositiveButtonText != null) {
                btnConfirm.setText(mPositiveButtonText);
                btnConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mPositiveButtonClickListener != null) {
                            mPositiveButtonClickListener.onClick(mIosDialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                        mIosDialog.dismiss();
                    }
                });
            }
            if (mNegativeButtonText != null) {
                btnCancel.setText(mNegativeButtonText);
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mNegativeButtonClickListener != null) {
                            mNegativeButtonClickListener.onClick(mIosDialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                        mIosDialog.dismiss();
                    }
                });
            }


            return mIosDialog;
        }

        public ios_style_alert_dialog_1 show() {
            mIosDialog = create();
            mIosDialog.show();
            return mIosDialog;
        }
    }

}
