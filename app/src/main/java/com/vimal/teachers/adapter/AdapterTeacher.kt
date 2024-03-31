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
import com.vimal.teachers.models.ModelTeacher
import com.vimal.teachers.util.Config
import com.vimal.teachers.util.Utils


@SuppressLint("NotifyDataSetChanged")
class AdapterTeacher(
    private val context: Context,
    private var modelLists: MutableList<ModelTeacher?>,
) : RecyclerView.Adapter<AdapterTeacher.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    private val repository: Repository = Repository.getInstance(context)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelLists[position]!!

        val ids = model.id!!.toIntOrNull() ?: 0

        holder.binding.tvTitle.text = context.getString(R.string.name, model.name)
        holder.binding.tvAge.text = context.getString(R.string.age, model.age)
        holder.binding.tvCoins.text = context.getString(R.string.coins, model.coins)

        try {
            val isFavorite = repository.isFavorite(ids)
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
            onItemClickListener?.onItemClick(model)
        }

        val modelFavorite = ModelFavorite(
            ids,
            model.name,
            model.language_name,
            model.age,
            model.profile_pic,
            model.gender,
            model.thumbnail,
            model.user_busy,
            model.per_minute_coins,
            model.coins
        )

        holder.binding.ivFavorite.setOnClickListener {
            try {
                val isFavorite = repository.isFavorite(ids)
                if (isFavorite) {
                    repository.deleteFavorite(modelFavorite)
                    holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                    Toast.makeText(context, "Remove to Favorite", Toast.LENGTH_SHORT).show()
                } else {
                    repository.insertFavorite(modelFavorite)
                    holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
                    Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Utils.getErrors(e)
            }
        }
    }

    override fun getItemCount(): Int {
        return modelLists.size
    }

    fun updateData(newData: MutableList<ModelTeacher?>) {
        modelLists = newData
        notifyDataSetChanged()
    }

    fun resetData() {
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<ModelTeacher>) {
        val startPos = itemCount
        modelLists.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }


    interface OnItemClickListener {
        fun onItemClick(model: ModelTeacher)
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}