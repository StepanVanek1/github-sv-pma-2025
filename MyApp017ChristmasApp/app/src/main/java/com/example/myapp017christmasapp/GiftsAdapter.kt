package com.example.myapp017christmasapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp017christmasapp.databinding.ItemGiftPersonBinding

data class PersonGift(
    val name: String,
    val giftReady: Boolean
)

class GiftsAdapter(
    private val items: List<PersonGift>
) : RecyclerView.Adapter<GiftsAdapter.GiftViewHolder>() {

    inner class GiftViewHolder(val binding: ItemGiftPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonGift) {
            binding.personNameText.text = item.name
            binding.giftCheckBox.isChecked = item.giftReady
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val binding = ItemGiftPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
