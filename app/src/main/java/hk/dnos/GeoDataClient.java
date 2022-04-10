package hk.dnos;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;

public class GeoDataClient extends GoogleApi implements Api.ApiOptions
{
    protected GeoDataClient(@NonNull Context context, Api<GeoDataClient> api, Looper looper) {
        super(context, api, looper);


    }
}

