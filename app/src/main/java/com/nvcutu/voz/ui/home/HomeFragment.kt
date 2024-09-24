package com.nvcutu.voz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nvcutu.voz.Current
import com.nvcutu.voz.R
import com.nvcutu.voz.adapters.HeaderAdapter
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.databinding.FragmentHomeBinding
import com.nvcutu.voz.models.HeaderHomeModel
import com.nvcutu.voz.viewmodel.HomeVm
import org.jsoup.HttpStatusException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var vm: HomeVm

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm = ViewModelProvider(this)[HomeVm::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vm.headersLiveData.observe(viewLifecycleOwner) { headers ->
            if (headers.isEmpty()) {
                loadHome()
                return@observe
            }

            val arrayList = arrayListOf<HeaderHomeModel>()
            val headerAdapter = HeaderAdapter(arrayList)
            headerAdapter.onItemClick = { headerItem ->
                val bundle = Bundle()
                bundle.putString("url", headerItem.url)
                findNavController().navigate(R.id.threadListFragment, bundle)
            }

            headerAdapter.onClick = { header, pos ->

            }

            binding.rcyHeaderHome.adapter = headerAdapter

            arrayList.addAll(headers)
        }

        return root
    }

    private fun loadHome() {
        Current.fetch.getHtml(Resource.URL_BASE) {
            when (it.result) {
                null -> {
                    it.error?.let { err ->
                        onLoadHtmlError(err)
                    }
                }

                else -> {
                    it.result?.let { doc ->
                        vm.document = doc
                        vm.getList()
                    }
                }
            }
        }
    }

    private fun onLoadHtmlError(httpStatusException: HttpStatusException) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}