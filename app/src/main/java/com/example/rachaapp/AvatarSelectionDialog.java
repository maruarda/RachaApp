package com.example.rachaapp;

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

public class AvatarSelectionDialog extends Dialog {

    // Interface to send data back to the Activity
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
                    // Get the ID from the tag
                    Object tag = v.getTag();
                    if (tag != null) {
                        int selectedId = Integer.parseInt(tag.toString());
                        // Notify activity and close dialog
                        listener.onAvatarSelected(selectedId);
                        dismiss();
                    }
                });
            }
        }
    }
}