package com.vimal.teachers.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.teachers.R
import com.vimal.teachers.databinding.ActivityProfileBinding
import com.vimal.teachers.models.ModelFavorite
import com.vimal.teachers.models.ModelTeacher
import com.vimal.teachers.util.Config
import com.vimal.teachers.util.Constant
import com.vimal.teachers.util.Utils

@Suppress("DEPRECATION")
class ActivityProfile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }


        when (val serializableExtra = intent.getSerializableExtra(Constant.EXTRA_KEY)) {
            is ModelFavorite -> {
                try {
                    Glide.with(this)
                        .load(Config.REST_API_URL + serializableExtra.profile_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(binding.ivProfileImage)

                    binding.tvProfileName.text = getString(R.string.name, serializableExtra.name)
                    binding.tvProfileAge.text = getString(R.string.age, serializableExtra.age)
                    binding.tvProfileCoins.text = getString(R.string.coins, serializableExtra.coins)
                    binding.tvProfileLanguage.text = serializableExtra.language_name

                } catch (e: Exception) {
                    Utils.getErrors(e)
                }
            }

            is ModelTeacher -> {
                try {
                    Glide.with(this)
                        .load(Config.REST_API_URL + serializableExtra.profile_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(binding.ivProfileImage)

                    binding.tvProfileName.text = getString(R.string.name, serializableExtra.name)
                    binding.tvProfileAge.text = getString(R.string.age, serializableExtra.age)
                    binding.tvProfileCoins.text = getString(R.string.coins, serializableExtra.coins)
                    binding.tvProfileLanguage.text = serializableExtra.language_name

                } catch (e: Exception) {
                    Utils.getErrors(e)
                }
            }

            else -> {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}