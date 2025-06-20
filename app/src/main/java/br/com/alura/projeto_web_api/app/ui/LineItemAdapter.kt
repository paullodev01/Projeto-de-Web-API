package br.com.alura.projeto_web_api.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.projeto_web_api.R // Importe seu R
import br.com.alura.projeto_web_api.app.data.LineItem // Importe seu modelo LineItem

// Usando ListAdapter para melhor performance com DiffUtil
class LineItemAdapter : ListAdapter<LineItem, LineItemAdapter.LineViewHolder>(LineItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_line, parent, false)
        return LineViewHolder(view)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val item = getItem(position) // getItem() é do ListAdapter
        holder.bind(item)
    }

    inner class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.iv_line_icon)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_line_title)

        fun bind(item: LineItem) {
            titleTextView.text = item.title

            // Lógica para carregar o ícone
            // Por enquanto, vamos usar um placeholder se o nome não for reconhecido.
            // No futuro, você pode ter um mapeamento mais robusto de item.icon (String) para Drawables.
            when (item.icon.uppercase()) { // Usar uppercase para ser case-insensitive
                "CONTRACT_AGREEMENT_ICON" -> iconImageView.setImageResource(R.drawable.ic_contract_agreement) // Exemplo, crie este drawable
                "BANK_ICON" -> iconImageView.setImageResource(R.drawable.ic_bank) // Exemplo, crie este drawable
                "WALLET_ICON" -> iconImageView.setImageResource(R.drawable.ic_wallet) // Exemplo, crie este drawable
                "CHECK_ICON" -> iconImageView.setImageResource(R.drawable.ic_check_circle) // Exemplo, crie este drawable
                // Adicione mais casos conforme necessário para seus ícones
                else -> iconImageView.setImageResource(R.drawable.ic_placeholder_icon) // Um ícone genérico de placeholder
            }

            // Exemplo de como controlar a visibilidade do separador se você o tivesse em item_line.xml
            // val separatorView: View = itemView.findViewById(R.id.line_separator)
            // separatorView.visibility = if (item.separator) View.VISIBLE else View.GONE
        }
    }

    // DiffUtil para calcular as diferenças entre listas de forma eficiente
    class LineItemDiffCallback : DiffUtil.ItemCallback<LineItem>() {
        override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            // Supondo que cada item pode ser identificado unicamente pelo título ou uma combinação
            // Se você tiver um ID único no LineItem, use-o aqui.
            return oldItem.title == newItem.title // Ajuste se houver um ID melhor
        }

        override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem == newItem // Data class faz a comparação de conteúdo
        }
    }
}