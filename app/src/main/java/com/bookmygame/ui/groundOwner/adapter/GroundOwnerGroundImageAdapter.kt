package com.bookmygame.ui.groundOwner.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmygame.R
import com.bookmygame.databinding.ListItemGroundImageBinding
import com.bookmygame.ui.groundOwner.GroundImageSource
import com.squareup.picasso.Picasso

class GroundOwnerGroundImageAdapter(private val images: MutableList<GroundImageSource>) :
    RecyclerView.Adapter<GroundImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundImageViewHolder {
        return GroundImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_ground_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroundImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    fun addImage(source: GroundImageSource) {
        images.add(source)
        notifyDataSetChanged()
    }

    fun getImageUris(): List<Uri> = images.filterIsInstance<GroundImageSource.Local>().map {
        it.uri
    }

    fun getImageUrls(): List<String> = images.filterIsInstance<GroundImageSource.URL>().map {
        it.url
    }

    fun getImagePaths(): List<String> {
        val imagePaths = mutableListOf<String>()
        for (image in images) {
            if (image is GroundImageSource.Local)
                imagePaths.add(image.uri.path!!)
        }

        return imagePaths
    }

    fun getImageUriPaths(): List<String> = images.filterIsInstance<GroundImageSource.Local>().map {
        it.uri.path!!
    }
}

class GroundImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ListItemGroundImageBinding.bind(itemView)

    fun bind(source: GroundImageSource) {
//        binding.groundImage.setImageURI(imageUri)
        when (source) {
            is GroundImageSource.Local -> Picasso.get().load(source.uri).into(binding.groundImage)
            is GroundImageSource.URL -> Picasso.get().load(source.url).into(binding.groundImage)
        }
    }
}