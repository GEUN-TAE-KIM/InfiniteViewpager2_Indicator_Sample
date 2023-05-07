package cream.geuntae.infinityviewpager2_indicator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cream.geuntae.infinityviewpager2_indicator.R

class ImageAdapter(private val images: Array<Int>) :
    RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_image,
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position % images.size])
    }


    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}