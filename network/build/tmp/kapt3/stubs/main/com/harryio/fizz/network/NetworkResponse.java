package com.harryio.fizz.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u001b\b\u0000\u0012\b\u0010\u0003\u001a\u0004\u0018\u00018\u0000\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\f\u001a\u00020\rR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0015\u0010\u0003\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000e"}, d2 = {"Lcom/harryio/fizz/network/NetworkResponse;", "T", "", "response", "errorStatusCode", "", "(Ljava/lang/Object;Ljava/lang/String;)V", "getErrorStatusCode", "()Ljava/lang/String;", "getResponse", "()Ljava/lang/Object;", "Ljava/lang/Object;", "isSuccessful", "", "network"})
public final class NetworkResponse<T extends java.lang.Object> {
    @org.jetbrains.annotations.Nullable()
    private final T response = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorStatusCode = null;
    
    public final boolean isSuccessful() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final T getResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorStatusCode() {
        return null;
    }
    
    public NetworkResponse(@org.jetbrains.annotations.Nullable()
    T response, @org.jetbrains.annotations.Nullable()
    java.lang.String errorStatusCode) {
        super();
    }
}