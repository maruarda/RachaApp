package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.rachapp.R;

public class AvatarSelectionDialog extends Dialog {

    public interface OnAvatarSelectedListener {
        void onAvatarSelected(int avatarId);
    }

    private final OnAvatarSelectedListener listener;

    public AvatarSelectionDialog(@NonNull Context context, OnAvatarSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_avatar_selector);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        setupGridClicks();
        findViewById(R.id.btnFecharDialog).setOnClickListener(v -> dismiss());
    }

    private void setupGridClicks() {
        ViewGroup grid = findViewById(R.id.gridAvataresDialog);
        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            if (child instanceof ImageView) {
                child.setOnClickListener(v -> {
                    Object tag = v.getTag();
                    if (tag != null) {
                        int selectedId = Integer.parseInt(tag.toString());
                        listener.onAvatarSelected(selectedId);
                        dismiss();
                    }
                });
            }
        }
    }
}