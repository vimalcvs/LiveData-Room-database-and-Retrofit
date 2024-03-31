package com.vimal.teachers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.teachers.R
import com.vimal.teachers.databinding.ItemListBinding
import com.vimal.teachers.db.Repository
import com.vimal.teachers.models.ModelFavorite
import com.vimal.teachers.util.Config
import com.vimal.teachers.util.Utils

@SuppressLint("NotifyDataSetChanged")

class AdapterFavorite(private val context: Context, private var list: List<ModelFavorite>) :

    RecyclerView.Adapter<AdapterFavorite.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private val repository: Repository = Repository.getInstance(context)!!

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvTitle.text = context.getString(R.string.name, model.name)
        holder.binding.tvAge.text = context.getString(R.string.age, model.age)
        holder.binding.tvCoins.text = context.getString(R.string.coins, model.coins)

        try {
            val isFavorite = repository.isFavorite(model.id)
            if (isFavorite) {
                holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
            } else {
                holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        } catch (e: Exception) {
            Utils.getErrors(e)
        }

        Glide.with(context)
            .load(Config.REST_API_URL + model.profile_pic)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.ivImage)


        holder.binding.cvCard.setOnClickListener {
            mOnItemClickListener?.onItemClick(model)
        }

        holder.binding.ivFavorite.setOnClickListener {
            try {
                repository.deleteFavorite(model)
                holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                Toast.makeText(context, "Remove to Favorite", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Utils.getErrors(e)
            }
        }
    }

    fun changeData(productList: List<ModelFavorite>) {
        this.list = productList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(modelWallpaper: ModelFavorite?)
        fun onItemDelete(modelWallpaper: ModelFavorite?)
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}