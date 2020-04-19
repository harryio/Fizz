package com.harryio.fizz.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u000b\u001a\u0004\b\u000e\u0010\u000fR#\u0010\u0011\u001a\n \u0007*\u0004\u0018\u00010\u00120\u00128BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0015\u0010\u000b\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0017"}, d2 = {"Lcom/harryio/fizz/network/NetworkInteractor;", "", "apiKey", "", "(Ljava/lang/String;)V", "movieService", "Lcom/harryio/fizz/network/MovieService;", "kotlin.jvm.PlatformType", "getMovieService", "()Lcom/harryio/fizz/network/MovieService;", "movieService$delegate", "Lkotlin/Lazy;", "okHttpClient", "Lokhttp3/OkHttpClient;", "getOkHttpClient", "()Lokhttp3/OkHttpClient;", "okHttpClient$delegate", "retrofit", "Lretrofit2/Retrofit;", "getRetrofit", "()Lretrofit2/Retrofit;", "retrofit$delegate", "Companion", "network"})
public final class NetworkInteractor {
    private final kotlin.Lazy movieService$delegate = null;
    private final kotlin.Lazy retrofit$delegate = null;
    private final kotlin.Lazy okHttpClient$delegate = null;
    private final java.lang.String apiKey = null;
    private static final kotlin.Lazy moshi$delegate = null;
    private static com.harryio.fizz.network.NetworkInteractor networkInteractor;
    public static final com.harryio.fizz.network.NetworkInteractor.Companion Companion = null;
    
    private final com.harryio.fizz.network.MovieService getMovieService() {
        return null;
    }
    
    private final retrofit2.Retrofit getRetrofit() {
        return null;
    }
    
    private final okhttp3.OkHttpClient getOkHttpClient() {
        return null;
    }
    
    public NetworkInteractor(@org.jetbrains.annotations.NotNull()
    java.lang.String apiKey) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR#\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048@X\u0080\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/harryio/fizz/network/NetworkInteractor$Companion;", "", "()V", "moshi", "Lcom/squareup/moshi/Moshi;", "kotlin.jvm.PlatformType", "getMoshi$network", "()Lcom/squareup/moshi/Moshi;", "moshi$delegate", "Lkotlin/Lazy;", "networkInteractor", "Lcom/harryio/fizz/network/NetworkInteractor;", "getMovieService", "Lcom/harryio/fizz/network/MovieService;", "apiKey", "", "network"})
    public static final class Companion {
        
        public final com.squareup.moshi.Moshi getMoshi$network() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.harryio.fizz.network.MovieService getMovieService(@org.jetbrains.annotations.NotNull()
        java.lang.String apiKey) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}