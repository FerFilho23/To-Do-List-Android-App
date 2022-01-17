package com.codinginflow.mvvmtodo.view.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding
import com.codinginflow.mvvmtodo.model.Task

//TODO: ListAdapter - usar quando se tem uma fonte de dados dinamica que envia uma lista nova a cada modificacao dos seus itens
class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder> (DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {    //Pass the root layout
        fun bind(task: Task) {
            binding.apply {
                checkbox.isChecked = task.completed
                taskViewName.text = task.name
                taskViewName.paint.isStrikeThruText = task.completed
                taskPriority.isVisible = task.priority
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {    //Analisa a diferenca entre os conteudos das listas de tasks
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id //Compara a task em si (id é unico para cada item independente do conteudo)

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =  oldItem == newItem //Compara o conteudo das tasks

    }

}