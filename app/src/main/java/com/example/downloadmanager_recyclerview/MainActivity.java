package com.example.downloadmanager_recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.downloadmanager_recyclerview.api.RetrofitClient;
import com.example.downloadmanager_recyclerview.adapters.DocumentoAdapter;
import com.example.downloadmanager_recyclerview.models.Documento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DocumentoAdapter adapter;
    List<Documento> data = new ArrayList<>();

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_documentos);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DocumentoAdapter(data);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ArrayList<String> permisoslist = new ArrayList<>();
        permisoslist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisoslist.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        fetchDocumentos();
        getPermission(permisoslist);
    }

    private void fetchDocumentos() {

        RetrofitClient.getRetrofitClient().getDocuments().enqueue(new Callback<List<Documento>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Documento>> call, @NonNull Response<List<Documento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Documento>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al cargar los datos" +
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPermission(List<String> permisosSolicitados) {

        ArrayList<String> listPermisosNOAprob = (ArrayList<String>) getPermisosNoAprobados(permisosSolicitados);
        if (!listPermisosNOAprob.isEmpty())
            if (Build.VERSION.SDK_INT >= 23)
                requestPermissions(listPermisosNOAprob.toArray(new String[0]), 1);
    }

    public List<String> getPermisosNoAprobados(List<String> listaPermisos) {
        ArrayList<String> list = new ArrayList<>();
        for (String permiso : listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                if (checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                    list.add(permiso);
        }
        return list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s = "";
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    s = s + "OK " + permissions[i] + "\n";
                else
                    s = s + "NO  " + permissions[i] + "\n";
            }
            Toast.makeText(this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    public void BajarDoc(String url, String nameDoc){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("PDF");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(nameDoc);
        request.setVisibleInDownloadsUi(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalFilesDir(this.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, "file.pdf");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),"Error: "  + e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

}