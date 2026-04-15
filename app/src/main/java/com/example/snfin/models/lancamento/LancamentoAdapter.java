package com.example.snfin.models.lancamento;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.R;

import java.util.List;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.ViewHolder> {

    private List<Lancamento> lista;

    public LancamentoAdapter(List<Lancamento> lista) {
        this.lista = lista;
    }

    public void updateList(List<Lancamento> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lancamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Lancamento l = lista.get(position);

        holder.tvData.setText(l.getData());
        holder.tvDescricao.setText(l.getDescricao());
        holder.tvValor.setText("R$ " + l.getValor());

        if ("DESPESA".equals(l.getNatureza())) {
            holder.tvValor.setTextColor(Color.RED);
        } else {
            holder.tvValor.setTextColor(Color.parseColor("#43a047"));
        }

        holder.tvStatus.setText(l.isPago() ? "Pago" : "Pendente");

        holder.btnPagar.setVisibility(l.isPago() ? View.GONE : View.VISIBLE);

        holder.btnPagar.setOnClickListener(v -> {
            // TODO: chamar API pagar
        });

        holder.btnFatura.setOnClickListener(v -> {
            // TODO: abrir tela fatura
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    // 🔥 VIEW HOLDER (faltava isso)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvData, tvDescricao, tvValor, tvStatus;
        Button btnPagar, btnFatura;

        public ViewHolder(View itemView) {
            super(itemView);

            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnPagar = itemView.findViewById(R.id.btnPagar);
            btnFatura = itemView.findViewById(R.id.btnFatura);
        }
    }
}