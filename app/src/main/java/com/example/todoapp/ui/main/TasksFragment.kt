package com.example.todoapp.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.utils.OnQueryTextChanged
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TasksFragment() : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var taskAdapter: TasksAdapter
    private lateinit var tasks: MutableLiveData<List<Task>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //AuthUI.getInstance().signOut(requireContext())

        initRecyclerView()

        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        val options = FirestoreRecyclerOptions.Builder<Task>()
            .setLifecycleOwner(this)
            .setQuery(TaskRepository().getTasks(), Task::class.java)
            .build()

        taskAdapter = TasksAdapter(options, object : TasksAdapter.OnItemClickListener{
            override fun onItemClick(task: Task) {

            }
        })

        val manager = LinearLayoutManager(requireContext())
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = manager
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {searchStr ->
            TaskRepository().getTasks(UserRepository.currentUser!!.uid, searchStr)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sort_by_name -> {

                true
            }
            R.id.action_sort_by_date_created -> {

                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked

                true
            }
            R.id.action_delete_all_completed_tasks -> {

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}