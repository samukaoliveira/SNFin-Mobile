package com.example.snfin.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.R;
import com.example.snfin.models.escala.EscalaSimples;

import java.util.List;

public class EscalasAdapter extends RecyclerView.Adapter<EscalasAdapter.EscalaViewHolder> {

    private List<EscalaSimples> escalas;
    private OnEscalaClickListener listener;

    public interface OnEscalaClickListener {
        void onEscalaClick(int escalaId);
    }

    public EscalasAdapter(List<EscalaSimples> escalas, OnEscalaClickListener listener) {
        this.escalas = escalas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EscalaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_escala_simples, parent, false);
        return new EscalaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EscalaViewHolder holder, int position) {
        EscalaSimples escala = escalas.get(position);

        holder.nome.setText(escala.getNome());
        holder.dataMinisterio.setText(escala.getData() + " - " +
               escala.getHora() + " - " + escala.getMinisterio());

        Context context = holder.itemView.getContext();

        if (escala.semFuncao()) {
            holder.funcao.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.funcao.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        holder.funcao.setText(escala.getFuncao());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEscalaClick(escala.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return escalas.size();
    }

    public static class EscalaViewHolder extends RecyclerView.ViewHolder {
        TextView nome, dataMinisterio, funcao, semFuncao;

        public EscalaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome_escala);
            dataMinisterio = itemView.findViewById(R.id.data_ministerio);
            funcao = itemView.findViewById(R.id.funcao);
        }
    }
}
