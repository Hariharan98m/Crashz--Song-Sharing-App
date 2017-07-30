package io.hasura.crashz.network;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import io.hasura.crashz.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public abstract class CustomResponseListener<T> implements Callback<T> {

    public abstract void onSuccessfulResponse(T response);

    public abstract void onFailureResponse(ErrorResponse errorResponse);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            onSuccessfulResponse(response.body());
        }
        else{
            try {
                String errorMessage = response.errorBody().string();
                try{
                    ErrorResponse errorResponse = new Gson().fromJson(errorMessage, ErrorResponse.class);
                    onFailureResponse(errorResponse);
                }catch (JsonSyntaxException jsonSyntaxException){
                    jsonSyntaxException.printStackTrace();
                    onFailureResponse(new ErrorResponse("JSON Syntax Exception"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                onFailureResponse(new ErrorResponse("IOException caught"));
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailureResponse(new ErrorResponse("You are not connected to the Internet"));
    }
}
