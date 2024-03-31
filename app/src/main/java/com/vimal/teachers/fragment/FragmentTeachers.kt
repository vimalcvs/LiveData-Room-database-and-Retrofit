package com.vimal.teachers.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimal.teachers.R
import com.vimal.teachers.activity.ActivityProfile
import com.vimal.teachers.adapter.AdapterTeacher
import com.vimal.teachers.callback.CallbackTeacher
import com.vimal.teachers.databinding.FragmentTeacherBinding
import com.vimal.teachers.models.ModelTeacher
import com.vimal.teachers.rest.RestAdapter
import com.vimal.teachers.util.Constant
import com.vimal.teachers.util.Constant.SUCCESS
import com.vimal.teachers.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentTeachers : Fragment(), AdapterTeacher.OnItemClickListener {

    private val modelLists: MutableList<ModelTeacher?> = ArrayList()
    private var _binding: FragmentTeacherBinding? = null
    private val binding get() = _binding!!
    private val adapterList: AdapterTeacher by lazy {
        AdapterTeacher(
            requireActivity(),
            modelLists
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        requestListPostApi()
    }

    private fun initViews() {
        try {
            binding.rvRecycler.layoutManager = LinearLayoutManager(requireActivity())
            binding.rvRecycler.adapter = adapterList
            adapterList.setOnItemClickListener(this)

            binding.slSwipe.setColorSchemeResources(
                R.color.color_orange,
                R.color.color_red,
                R.color.color_blue,
                R.color.color_green
            )

            binding.slSwipe.setOnRefreshListener {
                requestListPostApi()
                binding.included.pvProgress.visibility = View.GONE
            }

            binding.included.btError.setOnClickListener {

                requestListPostApi()

            }

        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    private fun requestListPostApi() {
        try {
            binding.included.pvProgress.visibility = View.VISIBLE
            val apiInterface = RestAdapter.createAPI(requireActivity())
            apiInterface.wallpapers!!.enqueue(object : Callback<CallbackTeacher?> {
                override fun onResponse(
                    call: Call<CallbackTeacher?>,
                    response: Response<CallbackTeacher?>
                ) {
                    binding.included.pvProgress.visibility = View.GONE
                    binding.included.llError.visibility = View.GONE
                    binding.rvRecycler.visibility = View.VISIBLE
                    binding.slSwipe.isRefreshing = false

                    val resp = response.body()
                    if (resp != null && resp.code == SUCCESS) {
                        displayApiResult(resp.user_data)
                    } else {
                        binding.included.llError.visibility = View.VISIBLE
                        binding.included.tvError.setText(R.string.no_internet_connection)
                        binding.included.ivError.setImageResource(R.drawable.ic_no_network)
                    }
                }

                override fun onFailure(call: Call<CallbackTeacher?>, t: Throwable) {
                    binding.slSwipe.isRefreshing = false
                    binding.included.pvProgress.visibility = View.GONE
                    binding.included.llError.visibility = View.VISIBLE
                    binding.included.ivError.setImageResource(R.drawable.ic_no_network)
                    binding.included.tvError.setText(R.string.no_internet_connection)
                }
            })
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }


    private fun displayApiResult(modelLists: List<ModelTeacher>) {
        try {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            val totalItems = modelLists.size
            val itemsPerBatch = 5
            val batches = (totalItems + itemsPerBatch - 1) / itemsPerBatch
            scope.launch {
                for (i in 0 until batches) {
                    val startIndex = i * itemsPerBatch
                    val endIndex = minOf((i + 1) * itemsPerBatch, totalItems)
                    val batchItems = ArrayList(modelLists.subList(startIndex, endIndex))
                    if (i != 0) {
                        delay(5000) // 1 second delay between batches
                    }
                    adapterList.addItems(batchItems)
                }
                binding.slSwipe.isRefreshing = false
                if (modelLists.isEmpty()) {
                    binding.included.llError.visibility = View.VISIBLE
                    binding.included.tvError.setText(R.string.no_data_available)
                }
            }
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }


    override fun onItemClick(model: ModelTeacher) {
        try {
            val intent = Intent(requireActivity(), ActivityProfile::class.java)
            intent.putExtra(Constant.EXTRA_KEY, model)
            startActivity(intent)
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    override fun onResume() {
        super.onResume()
        adapterList.resetData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.slSwipe.isRefreshing = false
        _binding = null
    }
}