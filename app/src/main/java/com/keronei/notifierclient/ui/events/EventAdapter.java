package com.keronei.notifierclient.ui.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.DataSource;
import androidx.paging.PagedListAdapter;

import com.keronei.notifierclient.R;
import com.keronei.notifierclient.data.Sdk;
import com.keronei.notifierclient.data.service.ActivityStarter;
import com.keronei.notifierclient.data.service.DateFormatHelper;
import com.keronei.notifierclient.ui.base.DiffByIdItemCallback;
import com.keronei.notifierclient.ui.base.ListItemWithSyncHolder;
import com.keronei.notifierclient.ui.event_form.EventFormActivity;
import com.keronei.notifierclient.ui.tracker_import_conflicts.TrackerImportConflictsAdapter;

import org.hisp.dhis.android.core.category.CategoryOptionCombo;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.program.ProgramStageDataElement;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;

import java.util.ArrayList;
import java.util.List;

import static com.keronei.notifierclient.data.service.StyleBinderHelper.setBackgroundColor;
import static com.keronei.notifierclient.data.service.StyleBinderHelper.setState;

public class EventAdapter extends PagedListAdapter<Event, ListItemWithSyncHolder> {

    private final AppCompatActivity activity;
    private DataSource<?, Event> source;

    public EventAdapter(AppCompatActivity activity) {
        super(new DiffByIdItemCallback<>());
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListItemWithSyncHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemWithSyncHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemWithSyncHolder holder, int position) {
        Event event = getItem(position);
        List<TrackedEntityDataValue> values = new ArrayList<>(event.trackedEntityDataValues());
        holder.title.setText(orgUnit(event.organisationUnit()).displayName());
        holder.subtitle1.setText(valueAt(values, event.programStage()));
        holder.subtitle2.setText(optionCombo(event.attributeOptionCombo()).displayName());
        holder.rightText.setText(DateFormatHelper.formatDate(event.eventDate()));
        holder.icon.setImageResource(R.drawable.ic_programs_black_24dp);
        holder.delete.setVisibility(View.VISIBLE);
        holder.delete.setOnClickListener(view -> {
            try {
                Sdk.d2().eventModule().events().uid(event.uid()).blockingDelete();
                invalidateSource();
                notifyDataSetChanged();
            } catch (D2Error d2Error) {
                d2Error.printStackTrace();
            }
        });
        setBackgroundColor(R.color.colorAccentDark, holder.icon);
        setState(event.state(), holder.syncIcon);
        setConflicts(event.uid(), holder);

        holder.itemView.setOnClickListener(view->{
            ActivityStarter.startActivity(
                    activity,
                    EventFormActivity.getFormActivityIntent(
                            activity,
                            event.uid(),
                            event.program(),
                            event.organisationUnit(),
                            EventFormActivity.FormType.CHECK
                    ),false
            );
        });
    }

    private OrganisationUnit orgUnit(String orgUnitUid) {
        return Sdk.d2().organisationUnitModule().organisationUnits().uid(orgUnitUid).blockingGet();
    }

    private String valueAt(List<TrackedEntityDataValue> values, String stageUid) {
        for (TrackedEntityDataValue dataValue : values) {
            ProgramStageDataElement programStageDataElement = Sdk.d2().programModule().programStageDataElements()
                    .byDataElement().eq(dataValue.dataElement())
                    .byProgramStage().eq(stageUid)
                    .one().blockingGet();
            if (programStageDataElement.displayInReports()) {
                return String.format("%s: %s", programStageDataElement.displayName(), dataValue.value());
            }
        }

        return null;
    }

    private CategoryOptionCombo optionCombo(String attrOptionCombo) {
        return Sdk.d2().categoryModule().categoryOptionCombos().uid(attrOptionCombo).blockingGet();
    }

    private void setConflicts(String trackedEntityInstanceUid, ListItemWithSyncHolder holder) {
        TrackerImportConflictsAdapter adapter = new TrackerImportConflictsAdapter();
        holder.recyclerView.setAdapter(adapter);
        adapter.setTrackerImportConflicts(Sdk.d2().importModule().trackerImportConflicts()
                .byTrackedEntityInstanceUid().eq(trackedEntityInstanceUid).blockingGet());
    }

    public void setSource(DataSource<?, Event> dataSource) {
        this.source = dataSource;
    }

    public void invalidateSource() {
        source.invalidate();
    }
}
