package com.harryio.fizz.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u001c\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00040\u00032\u0006\u0010\b\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Lcom/harryio/fizz/network/MovieService;", "", "createAuthenticationToken", "Lio/reactivex/Single;", "Lcom/harryio/fizz/network/ApiResponse;", "Lcom/harryio/fizz/network/response/AuthenticationTokenResponse;", "createSession", "Lcom/harryio/fizz/network/response/SessionResponse;", "createSessionRequest", "Lcom/harryio/fizz/network/request/CreateSessionRequest;", "network"})
public abstract interface MovieService {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "/3/authentication/token/new")
    public abstract io.reactivex.Single<com.harryio.fizz.network.ApiResponse<com.harryio.fizz.network.response.AuthenticationTokenResponse>> createAuthenticationToken();
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.POST(value = "/3/authentication/session/new")
    public abstract io.reactivex.Single<com.harryio.fizz.network.ApiResponse<com.harryio.fizz.network.response.SessionResponse>> createSession(@org.jetbrains.annotations.NotNull()
    com.harryio.fizz.network.request.CreateSessionRequest createSessionRequest);
}