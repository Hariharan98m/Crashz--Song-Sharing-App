package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 30-06-2017.
 */

public class DataHandlingResponse {
    @SerializedName("affected_rows")
    int affected_rows;
}
