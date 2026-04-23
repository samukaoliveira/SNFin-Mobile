package com.example.snfin.models.lancamento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snfin.CartaoDetalheActivity;
import com.example.snfin.LancamentoFormActivity;
import com.example.snfin.MainActivity;
import com.example.snfin.R;
import com.example.snfin.services.ApiService;
import com.example.snfin.services.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        holder.tvData.setText(l.getDataCurta());
        holder.tvDescricao.setText(l.getDescricao());
        holder.tvValor.setText("R$ " + l.getValor());

        if ("DESPESA".equals(l.getNatureza())) {
            holder.tvValor.setTextColor(Color.RED);
        } else {
            holder.tvValor.setTextColor(Color.parseColor("#43a047"));
        }

        // ===== STATUS PAGAMENTO =====
        if (l.isPago()) {
            holder.btnPagar.setColorFilter(
                    holder.itemView.getContext().getColor(R.color.verde_claro)
            );
        } else {
            holder.btnPagar.setColorFilter(
                    holder.itemView.getContext().getColor(R.color.cinza_texto)
            );
        }

        holder.btnPagar.setOnClickListener(v -> {
            // TODO pagar
        });

        // =========================================
        // 🔥 REGRA CORRETA (AGORA SIM)
        // =========================================
        boolean isCartao = "CARTAO".equals(l.getTipo());

        if (isCartao) {

            holder.btnEditar.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnPagar.setVisibility(View.GONE);

            holder.btnFatura.setVisibility(View.VISIBLE);

            holder.btnFatura.setOnClickListener(v -> {
                Intent intent = new Intent(context, CartaoDetalheActivity.class);

                // 🔥 agora vem da API
                intent.putExtra("cartaoId", l.getCartaoId());

                context.startActivity(intent);
            });

        } else {

            holder.btnEditar.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnPagar.setVisibility(View.VISIBLE);
            holder.btnFatura.setVisibility(View.GONE);

            holder.btnEditar.setOnClickListener(v -> abrirEdicao(l));

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirmar")
                        .setMessage("Deseja excluir este lançamento?")
                        .setPositiveButton("Sim", (dialog, which) -> {

                            int id = l.getId();

                            ApiService api = RetrofitInstance
                                    .getRetrofitInstance(v.getContext())
                                    .create(ApiService.class);

                            SharedPreferences sharedPref = v.getContext()
                                    .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

                            String token = sharedPref.getString("auth_token", null);

                            api.deletarLancamento("Token " + token, id)
                                    .enqueue(new Callback<Void>() {

                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {

                                                Toast.makeText(v.getContext(),
                                                        "Deletado com sucesso",
                                                        Toast.LENGTH_SHORT).show();

                                                int pos = holder.getAdapterPosition();
                                                lista.remove(pos);
                                                notifyItemRemoved(pos);

                                            } else {
                                                Toast.makeText(v.getContext(),
                                                        "Erro ao deletar",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(v.getContext(),
                                                    "Falha: " + t.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });

                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
        }
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
        Button btnFatura;

        ImageButton  btnEditar, btnDelete, btnPagar;

        public ViewHolder(View itemView) {
            super(itemView);

            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvValor = itemView.findViewById(R.id.tvValor);

            btnPagar = itemView.findViewById(R.id.btnPagar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnFatura = itemView.findViewById(R.id.btnFatura);
        }
    }
}