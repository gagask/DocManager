package com.example.docmanager.ui.changes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.docmanager.FilesInfoAdapter
import com.example.docmanager.MyViewModelFactory
import com.example.docmanager.R
import com.example.docmanager.databinding.FragmentChangesBinding
import com.example.docmanager.ui.storage.StorageViewModel

class ChangesFragment : Fragment() {

    private var _binding: FragmentChangesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: ChangesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;

        val viewModelFactory = MyViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[ChangesViewModel::class.java]
        binding.viewModel = viewModel



        val filterButtons = arrayOf(binding.typeAllButton, binding.typeAddedButton, binding.typeDeletedButton, binding.typeUpdatedButton)
        filterButtons.map { it ->
            it.setOnClickListener {
            viewModel.filterFiles((it as Button).text as String)
        }}

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
                    //viewModel.sortFiles()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

        }

        binding.filesRecyclerView.adapter = FilesInfoAdapter(
            FilesInfoAdapter.OnClickListener {})

        binding.orderButton.setOnClickListener {
            viewModel.changeOrder()
            //viewModel.sortFiles()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}