package com.example.yoshi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VoteAdapter(private val votes: List<VoteData>) : RecyclerView.Adapter<VoteAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val tags: TextView = itemView.findViewById(R.id.tags)
        val references: TextView = itemView.findViewById(R.id.references)
        val description: TextView = itemView.findViewById(R.id.description)
        // Add an ImageView if you have one in your layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vote_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vote = votes[position]
        holder.title.text = vote.title
        holder.tags.text = vote.tags
        holder.references.text = vote.references
        holder.description.text = vote.description
        // If you have an ImageView, you can load the image using Glide
        // Glide.with(holder.itemView.context).load(vote.image).into(holder.imageView)
    }

    override fun getItemCount() = votes.size
}
