package com.example.downloadmanager_recyclerview.models;

import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.downloadmanager_recyclerview.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Permisos extends AppCompatActivity {
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
            Toast.makeText(MainActivity.mainActivity.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
