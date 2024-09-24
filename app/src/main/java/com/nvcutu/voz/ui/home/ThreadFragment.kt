package com.nvcutu.voz.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nvcutu.voz.Current
import com.nvcutu.voz.R
import com.nvcutu.voz.adapters.ThreadItemAdapter
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.databinding.FragmentThreadBinding
import com.nvcutu.voz.models.ThreadItemModel
import com.nvcutu.voz.ui.BaseFragment
import com.nvcutu.voz.viewmodel.ThreadVm
import org.jsoup.HttpStatusException

class ThreadFragment : BaseFragment() {

    private var _binding: FragmentThreadBinding? = null
    private lateinit var vm: ThreadVm

    private var arrayList = arrayListOf<ThreadItemModel>()
    private lateinit var threadItemAdapter: ThreadItemAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm = ViewModelProvider(this)[ThreadVm::class.java]
        _binding = FragmentThreadBinding.inflate(inflater, container, false)

        threadItemAdapter = ThreadItemAdapter(arrayList, lifecycleScope)
        binding.rcyThreadList.adapter = threadItemAdapter

        val root: View = binding.root
        arguments?.getString("url")?.let { vm.url = it }

        vm.titleLiveData.observe(viewLifecycleOwner) { title ->
            setTitle(title)
        }

        vm.titleLiveData.postValue(getString(R.string.txt_loading))

        vm.threadsLiveData.observe(viewLifecycleOwner) { posts ->
            if (posts.isEmpty()) {
                loadContent(Resource.URL_BASE + vm.url)
                return@observe
            }

            arrayList.clear()
            arrayList.addAll(posts)
            threadItemAdapter.notifyDataSetChanged()
            binding.rcyThreadList.scrollToPosition(0)
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

        loadContent(Resource.URL_BASE + vm.url)

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