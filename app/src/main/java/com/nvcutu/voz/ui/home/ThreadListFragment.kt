package com.nvcutu.voz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nvcutu.voz.Current
import com.nvcutu.voz.R
import com.nvcutu.voz.adapters.ThreadListItemAdapter
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.databinding.FragmentThreadListBinding
import com.nvcutu.voz.models.ThreadListItemModel
import com.nvcutu.voz.ui.BaseFragment
import com.nvcutu.voz.viewmodel.ThreadListVm
import org.jsoup.HttpStatusException

class ThreadListFragment : BaseFragment() {

    private var _binding: FragmentThreadListBinding? = null
    private lateinit var vm: ThreadListVm

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm = ViewModelProvider(this)[ThreadListVm::class.java]
        _binding = FragmentThreadListBinding.inflate(inflater, container, false)

        val root: View = binding.root
        arguments?.getString("url")?.let { vm.url = it }

        vm.titleLiveData.observe(viewLifecycleOwner) { title ->
            setTitle(title)
        }

        vm.threadsLiveData.observe(viewLifecycleOwner) { threads ->
            if (threads.isEmpty()) {
                loadContent(Resource.URL_BASE + vm.url)
                return@observe
            }

            val arrayList = arrayListOf<ThreadListItemModel>()
            val threadsAdapter = ThreadListItemAdapter(arrayList)

            threadsAdapter.onItemClick = { thead ->
                val bundle = Bundle()
                bundle.putString("url", thead.threadUrl)
                findNavController().navigate(R.id.threadFragment, bundle)
            }

            binding.rcyThreadList.adapter = threadsAdapter
            arrayList.addAll(threads)
        }

        binding.acBottomBar.onPageItemClick = { page ->
            loadContent("${Resource.URL_BASE}${vm.url}page-${page}")
        }

        vm.pageLiveData.observe(viewLifecycleOwner) { page ->
            binding.acBottomBar.setPage(page)

            binding.acBottomBar.onFirstPageClick = {
                loadContent("${Resource.URL_BASE}${vm.url}")
            }

            binding.acBottomBar.onLastPageClick = {
                loadContent("${Resource.URL_BASE}${vm.url}page-${page.maxPage}")
            }
        }

        return root
    }

    private fun loadContent(url: String) {
        Current.fetch.getHtml(url) {
            when (it.result) {
                null -> {
                    it.error?.let { err ->
                        onLoadHtmlError(err)
                    }
                }

                else -> {
                    it.result?.let { doc ->
                        vm.document = doc
                        vm.getTitle()
                        vm.getList()
                        vm.getPage()
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