package com.vimal.teachers.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimal.teachers.R
import com.vimal.teachers.activity.ActivityProfile
import com.vimal.teachers.adapter.AdapterFavorite
import com.vimal.teachers.databinding.FragmentFavoriteBinding
import com.vimal.teachers.db.Repository
import com.vimal.teachers.models.ModelFavorite
import com.vimal.teachers.util.Constant
import com.vimal.teachers.util.Utils

class FragmentFavorite : Fragment(), AdapterFavorite.OnItemClickListener {

    private val repository: Repository by lazy { Repository.getInstance(requireActivity())!! }
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val adapter: AdapterFavorite by lazy { AdapterFavorite(requireActivity(), ArrayList()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun initViews() {
        try {
            binding.rvRecycler.layoutManager = LinearLayoutManager(requireActivity())
            binding.rvRecycler.adapter = adapter
            adapter.setOnItemClickListener(this)
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    private fun observeData() {
        try {
            repository.allFavorite().observe(viewLifecycleOwner) { products ->
                if (products.isNotEmpty()) {
                    adapter.changeData(products)
                    binding.included.llError.visibility = View.GONE
                } else {
                    adapter.changeData(ArrayList())
                    binding.included.llError.visibility = View.VISIBLE
                    binding.included.tvError.setText(R.string.no_favorites_available)
                    binding.included.ivError.setImageResource(R.drawable.ic_no_favorite)
                    binding.included.btError.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    override fun onItemClick(modelFavorite: ModelFavorite?) {
        try {
            val intent = Intent(requireActivity(), ActivityProfile::class.java)
            intent.putExtra(Constant.EXTRA_KEY, modelFavorite)
            startActivity(intent)
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    override fun onItemDelete(modelFavorite: ModelFavorite?) {
        try {
            repository.deleteFavorite(modelFavorite)
            Toast.makeText(requireActivity(), "Deleted to Favorite", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}