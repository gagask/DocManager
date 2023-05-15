package com.example.docmanager.ui.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.docmanager.FilesInfoAdapter
import com.example.docmanager.MyViewModelFactory
import com.example.docmanager.R
import com.example.docmanager.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: StorageViewModel
    private lateinit var adapter: FilesInfoAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;

        val viewModelFactory = MyViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[StorageViewModel::class.java]
        binding.viewModel = viewModel

        binding.filesRecyclerView.adapter = FilesInfoAdapter(
            FilesInfoAdapter.OnClickListener {
                if (it.isDirectory) {
                    viewModel.navTo(it.path)
                }
                else{
                    // TODO open file somehow
                }

            })

        binding.sortTypeSpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.sort_types, android.R.layout.select_dialog_item)

        binding.sortTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    viewModel.sortType = parent.getItemAtPosition(position).toString()
                    viewModel.sortFiles()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

        }

        binding.orderButton.setOnClickListener {
            viewModel.changeOrder()
            viewModel.sortFiles()
        }

        binding.backButton.setOnClickListener {
            viewModel.navBack()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}