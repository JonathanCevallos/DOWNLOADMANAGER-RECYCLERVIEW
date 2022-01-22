package com.example.downloadmanager_recyclerview.api;

import com.example.downloadmanager_recyclerview.models.Documento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("documentos")
    Call<List<Documento>> getDocuments();
}
