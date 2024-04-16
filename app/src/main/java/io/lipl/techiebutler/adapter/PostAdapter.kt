package io.lipl.techiebutler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.lipl.techiebutler.databinding.LayoutPostBinding
import io.lipl.techiebutler.domain.model.PostItem

class PostAdapter(
    private var list: MutableList<PostItem?>?,
    val context: Context,
    val onItemClicked: (Int, View) -> Unit
) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostAdapter.MyHolder {
        val binding = LayoutPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.MyHolder, position: Int) {
        with(holder.binding) {

            list!![position]?.let {
                tvId.text = it.id.toString()
                tvTitle.text = it.title.toString()
                tvBody.text = it.body.toString()
            }

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class MyHolder(val binding: LayoutPostBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            //  click
            itemView.setOnClickListener {
                onItemClicked(adapterPosition, binding.clMain)
            }
        }

    }


}