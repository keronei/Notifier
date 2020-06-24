package com.keronei.notifierclient.ui.foreign_key_violations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.keronei.notifierclient.R;
import com.keronei.notifierclient.data.service.DateFormatHelper;
import com.keronei.notifierclient.ui.base.DiffByIdItemCallback;
import com.keronei.notifierclient.ui.base.ListItemHolder;

import org.hisp.dhis.android.core.maintenance.ForeignKeyViolation;

import static com.keronei.notifierclient.data.service.StyleBinderHelper.setBackgroundColor;

public class ForeignKeyViolationsAdapter extends PagedListAdapter<ForeignKeyViolation, ListItemHolder> {

    ForeignKeyViolationsAdapter() {
        super(new DiffByIdItemCallback<>());
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        ForeignKeyViolation fkViolation = getItem(position);
        holder.title.setText(fkViolation.notFoundValue());
        holder.subtitle1.setText(fkViolation.fromTable() + "." + fkViolation.fromColumn());
        holder.subtitle2.setText(fkViolation.toTable() + "." + fkViolation.toColumn());
        holder.rightText.setText(DateFormatHelper.formatDate(fkViolation.created()));
        holder.icon.setImageResource(R.drawable.ic_foreign_key_black_24dp);
        setBackgroundColor(R.color.colorAccentDark, holder.icon);
    }
}
