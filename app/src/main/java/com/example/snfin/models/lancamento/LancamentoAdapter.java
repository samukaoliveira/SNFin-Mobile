package com.example.snfin.models.lancamento;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.LancamentoFormActivity;
import com.example.snfin.MainActivity;
import com.example.snfin.R;

import java.util.List;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.ViewHolder> {

    private List<Lancamento> lista;
    private Context context;

    public LancamentoAdapter(Context context, List<Lancamento> lista) {
        this.lista = lista;
        this.context = context;
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

        // 🔥 CLICK NO ITEM → EDITAR
        holder.btnEditar.setOnClickListener(v -> abrirEdicao(l));
    }

    private void abrirEdicao(Lancamento l) {

        Intent intent = new Intent(context, LancamentoFormActivity.class);

        intent.putExtra("id", l.getId());
        intent.putExtra("descricao", l.getDescricao());
        intent.putExtra("valor", l.getValor());
        intent.putExtra("data", l.getData());
        intent.putExtra("pago", l.isPago());
        intent.putExtra("natureza", l.getNatureza());
        intent.putExtra("fixo", l.getFixo());
        intent.putExtra("parcelas", l.getParcelas());

        // 🔥 ESSENCIAL: usar launcher da MainActivity
        if (context instanceof MainActivity) {
            ((MainActivity) context).launcher.launch(intent);
        } else {
            context.startActivity(intent); // fallback
        }
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvData, tvDescricao, tvValor, tvStatus;
        Button btnPagar, btnFatura, btnEditar;

        public ViewHolder(View itemView) {
            super(itemView);

            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnPagar = itemView.findViewById(R.id.btnPagar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnFatura = itemView.findViewById(R.id.btnFatura);
        }
    }
}