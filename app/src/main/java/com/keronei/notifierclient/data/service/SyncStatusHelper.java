package com.keronei.notifierclient.data.service;

import com.keronei.notifierclient.data.Sdk;

import org.hisp.dhis.android.core.common.State;

import java.util.Collections;

public class SyncStatusHelper {

    public static int programCount() {
        return Sdk.d2().programModule().programs().blockingCount();
    }

    public static int dataSetCount() {
        return Sdk.d2().dataSetModule().dataSets().blockingCount();
    }

    public static int trackedEntityInstanceCount() {
        return Sdk.d2().trackedEntityModule().trackedEntityInstances()
                .byState().neq(State.RELATIONSHIP).blockingCount();
    }

    public static int singleEventCount() {
        return Sdk.d2().eventModule().events().byEnrollmentUid().isNull().blockingCount();
    }

    public static int dataValueCount() {
        return Sdk.d2().dataValueModule().dataValues().blockingCount();
    }

    public static boolean isThereDataToUpload() {
        return Sdk.d2().trackedEntityModule().trackedEntityInstances().byState()
                .notIn(Collections.singletonList(State.SYNCED)).blockingCount() > 0 ||
                Sdk.d2().dataValueModule().dataValues().byState()
                        .notIn(Collections.singletonList(State.SYNCED)).blockingCount() > 0;
    }
}
