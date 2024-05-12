package com.example.taskmanager.adp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codility.todoapp.databinding.ListItemBinding
import com.example.taskmanager.databinding.ListItemBinding
import com.example.taskmanager.help.TaskDao
import com.example.taskmanager.help.Task
//import com.google.android.gms.tasks.Task


class Adp(private var taskList: ArrayList<Task>) : RecyclerView.Adapter<Adp.ViewHolder>() {

    private var listener: OnClickListener? = null

    fun setListener(clickListener: OnClickListener) {
        this.listener = clickListener
    }

    fun updateData(newTodoList: List<TaskDao>) {
        taskList.clear()
        taskList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task = taskList[position]
        holder.bindItems(task)

        holder.binding.root.setOnClickListener {
            listener?.onItemClick(task, position)
        }

        holder.binding.btnDelete.setOnClickListener {
            listener?.onItemDelete(task)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDesc.text = task.description
            binding.tvTimestamp.text = task.timestamp
        }
    }

    interface OnClickListener {
        fun onItemClick(task: Task, position: Int)
        fun onItemDelete(task: Task
    }
}