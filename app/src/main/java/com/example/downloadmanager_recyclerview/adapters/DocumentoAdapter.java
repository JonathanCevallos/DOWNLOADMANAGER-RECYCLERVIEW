package com.example.downloadmanager_recyclerview.adapters;

import static com.example.downloadmanager_recyclerview.MainActivity.mainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloadmanager_recyclerview.R;
import com.example.downloadmanager_recyclerview.models.Documento;

import java.util.List;


public class DocumentoAdapter extends RecyclerView.Adapter<DocumentoAdapter.ViewHolder> {

    public DocumentoAdapter(List<Documento> data) {
        this.data = data;
    }

    private List<Documento> data;
    public static Context context;



    @NonNull
    @Override
    public DocumentoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentoAdapter.ViewHolder holder, int position) {
        holder.txt_id.setText(data.get(position).getId());
        holder.txt_descripcion.setText(data.get(position).getDescripcion());
        holder.txt_fecha.setText(data.get(position).getFecha());

        //Escuchar el evento click del img_documento
        holder.img_documento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL_TO_DOWNLOAD = data.get(position).getUrlDocumento();
                String NAME_FILE = data.get(position).getUrlDocumento();
                mainActivity.BajarDoc(URL_TO_DOWNLOAD,NAME_FILE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_id, txt_descripcion, txt_fecha;
        ImageView img_documento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_descripcion = itemView.findViewById(R.id.txt_descripcion);
            txt_fecha = itemView.findViewById(R.id.txt_fecha);
            img_documento = itemView.findViewById(R.id.img_documento);
        }

    }
}
