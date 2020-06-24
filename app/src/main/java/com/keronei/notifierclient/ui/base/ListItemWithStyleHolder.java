package com.keronei.notifierclient.ui.base;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.keronei.notifierclient.R;

public class ListItemWithStyleHolder extends BaseListItemHolder {

    public final FrameLayout cardFrame;
    public final CardView card;

    public ListItemWithStyleHolder(@NonNull View view) {
        super(view);
        cardFrame = view.findViewById(R.id.cardFrame);
        card = view.findViewById(R.id.card);
    }
}