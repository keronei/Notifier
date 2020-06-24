package com.keronei.notifierclient.ui.data_sets.instances;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedListAdapter;

import com.keronei.notifierclient.R;
import com.keronei.notifierclient.data.service.ActivityStarter;
import com.keronei.notifierclient.ui.base.DiffByIdItemCallback;
import com.keronei.notifierclient.ui.base.ListItemWithSyncHolder;
import com.keronei.notifierclient.ui.data_sets.instances.data_set_form.DataSetFormActivity;

import org.hisp.dhis.android.core.dataset.DataSetInstance;

import java.text.MessageFormat;

import static com.keronei.notifierclient.data.service.StyleBinderHelper.setBackgroundColor;
import static com.keronei.notifierclient.data.service.StyleBinderHelper.setState;

public class DataSetInstancesAdapter extends PagedListAdapter<DataSetInstance, ListItemWithSyncHolder> {

    private final AppCompatActivity activity;

    DataSetInstancesAdapter(AppCompatActivity activity) {
        super(new DiffByIdItemCallback<>());
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListItemWithSyncHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListItemWithSyncHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemWithSyncHolder holder, int position) {
        DataSetInstance dataSetInstance = getItem(position);
        holder.title.setText(MessageFormat.format("{0} - {1}",
                dataSetInstance.period(), dataSetInstance.periodType().name()));
        holder.subtitle1.setText(dataSetInstance.organisationUnitDisplayName());
        holder.subtitle2.setText(dataSetInstance.attributeOptionComboDisplayName());
        holder.icon.setImageResource(R.drawable.ic_assignment_black_24dp);
        setBackgroundColor(R.color.colorAccentDark, holder.icon);
        holder.rightText.setText(dataSetInstance.valueCount().toString());
        setState(dataSetInstance.state(), holder.syncIcon);

        holder.itemView.setOnClickListener(view -> {
            ActivityStarter.startActivity(
                    activity,
                    DataSetFormActivity.getFormActivityIntent(
                            activity,
                            dataSetInstance.dataSetUid(),
                            dataSetInstance.period(),
                            dataSetInstance.organisationUnitUid(),
                            dataSetInstance.attributeOptionComboUid()),
                    false
            );
        });
    }
}
