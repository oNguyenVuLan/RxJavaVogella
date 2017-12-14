package com.example.framgianguyenvulan.rxvogella.api;

import android.util.Log;


import com.example.framgianguyenvulan.rxvogella.exception.ErrorResponse;
import com.example.framgianguyenvulan.rxvogella.exception.ResponseException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by TienDQ on 7/14/17.
 */

public class ServiceErrorHandlerFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private ServiceErrorHandlerFactory() {
        original = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    public static CallAdapter.Factory create() {
        return new ServiceErrorHandlerFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
        private final Retrofit retrofit;
        private final CallAdapter<R, Object> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<R, Object> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Object adapt(Call<R> call) {

            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                @Override
                public ObservableSource apply(@NonNull Throwable throwable) throws Exception {
                    return Observable.error(convertToResponseException(throwable));
                }
            });
        }

        private ResponseException convertToResponseException(Throwable throwable) {
            if (throwable instanceof ResponseException) {
                return (ResponseException) throwable;
            }

            if (throwable instanceof IOException) {
                try {
                    return ResponseException.Companion.getNetworkError(throwable);
                } catch (IllegalStateException | OnErrorNotImplementedException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
            }

            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                if (response.errorBody() == null) {
                    return ResponseException.Companion.getHttpError(response);
                }
                try {
                    String errorResponseData = response.errorBody().string();
                    ErrorResponse errorResponse = deserializeErrorBody(errorResponseData);
                    if (errorResponse != null && errorResponse.isError()) {
                        //Get error data from Server
                        return ResponseException.Companion.getServerError(errorResponse);
                    } else {
                        //Get error data cause http connection
                        return ResponseException.Companion.getHttpError(response);
                    }
                } catch (IOException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
            }

            return ResponseException.Companion.getUnexpectedError(throwable);
        }

        private ErrorResponse deserializeErrorBody(String errorString) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(errorString, ErrorResponse.class);
            } catch (JsonSyntaxException e) {
                return null;
            }
        }
    }

}
