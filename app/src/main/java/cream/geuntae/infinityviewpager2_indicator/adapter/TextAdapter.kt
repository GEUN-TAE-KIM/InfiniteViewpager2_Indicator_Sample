package cream.geuntae.infinityviewpager2_indicator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cream.geuntae.infinityviewpager2_indicator.data.Texts
import cream.geuntae.infinityviewpager2_indicator.databinding.ItemTextBinding

class TextAdapter(private val items: List<Texts>) :
    RecyclerView.Adapter<TextAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Texts) {
            binding.title.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position % items.size])
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}