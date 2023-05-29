package com.example.todoapp.ui.task

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.repository.mapFromFirebaseUser
import com.example.todoapp.utils.onQueryTextChanged
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class TasksFragment() : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var taskAdapter: TasksAdapter
    private val viewModel: TasksViewModel by viewModels() {
        TasksViewModel.TasksViewModelFactory(TaskRepository())
    }
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)

        val user = FirebaseAuth.getInstance().currentUser

        if(user == null){
            val action = TasksFragmentDirections.actionTasksFragmentToLoginFragment2()
            findNavController().navigate(action)
        } else {
            UserRepository.currentUser = mapFromFirebaseUser(user)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //AuthUI.getInstance().signOut(requireContext())

        /*val taskR = TaskRepository()
        taskR.createTask(Task(name = "Call mom", important = true))
        taskR.createTask(Task(name = "Breakfast", completed = true))
        taskR.createTask(Task(name = "Homework"))
        taskR.createTask(Task(name = "Pool", completed = true))*/

        taskAdapter = TasksAdapter(this)

        val manager = LinearLayoutManager(requireContext())
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = manager
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.differ.currentList[viewHolder.absoluteAdapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)

            fabAddTask.setOnClickListener{
                viewModel.onAddNewTaskClick()
            }
        }

        setFragmentResultListener("add_edit_request"){_, bundle ->
            val result = bundle.getInt("add_edit_request")
            viewModel.onAddEditResult(result)
        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner){
            taskAdapter.differ.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect{event ->
                when(event){
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage ->{
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment()
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(event.task)
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = TasksFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }


        setHasOptionsMenu(true)
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskChecked(task, isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery
        if(pendingQuery.isNotEmpty() && pendingQuery != null){
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged { searchInput ->
            viewModel.searchQuery = searchInput
            viewModel.onTaskSearched(searchInput)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.title){
            TasksViewModel.SORT_BY_NAME -> {
                viewModel.onSortTasksByName()
                true
            }
            TasksViewModel.SORT_BY_DATE_CREATED -> {
                viewModel.onSortTasksByDate()
                true
            }
            TasksViewModel.SORT_BY_IMPORTANCE -> {
                viewModel.onSortTasksByImportance()
                true
            }
            TasksViewModel.HIDE_COMPLETED -> {
                item.isChecked = !item.isChecked

                if(item.isChecked){
                    viewModel.onHideAllCompleted()
                } else {
                    viewModel.onSortTasksByImportance()
                }

                true
            }
            TasksViewModel.DELETE_ALL_COMPLETED -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}