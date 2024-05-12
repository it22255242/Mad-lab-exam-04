package com.example.taskmanager

import adp
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ActivityScrollBinding
import com.example.taskmanager.help.Task
import com.example.taskmanager.help.TaskDao
import com.example.taskmanager.help.TaskDatabase
import com.example.taskmanager.adp.Adp
import com.example.taskmanager.help.TaskDao
import com.example.taskmanager.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ScrollActivity : AppCompatActivity(), Adp.OnClickListener {

    private lateinit var binding: ActivityScrollBinding
    private lateinit var todoDao: TaskDao
    private lateinit var myAdapter: Adp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val db = TaskDatabase.getInstance(this)
        todoDao = db.todoDao()

        setupRecyclerView()

        binding.fab.setOnClickListener {
            showNoteDialog(false, null, -1)
        }

        getTodoList()
    }

    private fun setupRecyclerView() {
        binding.list.layoutManager = LinearLayoutManager(this)
        myAdapter = Adp(ArrayList())
        myAdapter.setListener(this)
        binding.list.adap = myAdapter
    }

    private fun getTodoList() {
        CoroutineScope(Dispatchers.IO).launch {
            val todoList = todoDao.getAllTodos()
            CoroutineScope(Dispatchers.Main).launch {
                myAdapter.updateData(todoList)
            }
        }
    }

    private fun deleteConfirmation(todo: Task) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete...")
        alertDialog.setMessage("Are you sure you want to delete this?")
        alertDialog.setIcon(R.drawable.ic_delete)
        alertDialog.setPositiveButton("YES") { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch {
                todoDao.delete(todo)
                getTodoList() // Refresh the list
            }
        }

        alertDialog.setNegativeButton("NO") { dialog, which ->
            dialog.cancel() // Cancel the dialog
        }
        alertDialog.show()
    }

    private fun showNoteDialog(shouldUpdate: Boolean, todo: Task?, position: Int) {
        val view = LayoutInflater.from(applicationContext).inflate(R.layout.add_task, null)
        val alertDialogView = AlertDialog.Builder(this).create()
        alertDialogView.setView(view)

        val tvHeader = view.findViewById<TextView>(R.id.tvHeader)
        val edTitle = view.findViewById<EditText>(R.id.edTitle)
        val edDesc = view.findViewById<EditText>(R.id.edDesc)
        val btAddUpdate = view.findViewById<Button>(R.id.btAddUpdate)
        val btCancel = view.findViewById<Button>(R.id.btCancel)

        if (shouldUpdate) btAddUpdate.text = "Update" else btAddUpdate.text = "Save"

        if (shouldUpdate && task != null) {
            edTitle.setText(task.title)
            edDesc.setText(task.description)
        }

        btAddUpdate.setOnClickListener {
            val title = edTitle.text.toString()
            val desc = edDesc.text.toString()

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Enter Your Title!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(desc)) {
                Toast.makeText(this, "Enter Your Description!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTask = if (shouldUpdate && task != null) {
                task.copy(title = title, description = desc)
            } else {
                Task(title = title, description = desc, timestamp = "") // Provide a timestamp value
            }

            if (shouldUpdate && task != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.update(newTask)
                    getTodoList() // Refresh the list
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.insert(newTask)
                    getTodoList() // Refresh the list
                }
            }
            alertDialogView.dismiss()
        }

        btCancel.setOnClickListener {
            alertDialogView.dismiss()
        }
        tvHeader.text = if (!shouldUpdate) getString(R.string.lbl_new_task_title) else getString(R.string.lbl_edit_task_title)

        alertDialogView.setCancelable(false)
        alertDialogView.show()
    }


    override fun onItemDelete(todo: Task) {
        deleteConfirmation(todo)
    }

    override fun onItemClick(todo: Task, position: Int) {
        showNoteDialog(true, todo, position)
    }
}