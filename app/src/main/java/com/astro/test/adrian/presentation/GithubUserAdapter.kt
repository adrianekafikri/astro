package com.astro.test.adrian.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.astro.test.adrian.R
import com.astro.test.adrian.databinding.ItemGithubUserBinding
import javax.inject.Inject

/**
 * Created by adrianekafikri on 03/03/22.
 */
class GithubUserAdapter @Inject constructor() :
    ListAdapter<GithubUserViewParam, GithubUserAdapter.GithubUserViewHolder>(
        GithubUserViewParam()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        return GithubUserViewHolder(ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<GithubUserViewParam>?) {
        if (list == null) super.submitList(null)
        else super.submitList(ArrayList(list))
    }

    inner class GithubUserViewHolder(
        private val binding: ItemGithubUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GithubUserViewParam) {
            with(binding) {
                tvName.text = item.name
                ivUser.context.imageLoader.enqueue(
                    ImageRequest.Builder(ivUser.context)
                        .data(item.avatar)
                        .target(
                            onStart = {
                                setDefaultImage(ivUser)
                            },
                            onSuccess = {
                                ivUser.setImageDrawable(it)
                            },
                            onError = {
                                setDefaultImage(ivUser)
                            }
                        )
                        .build()
                )
            }
        }

        private fun setDefaultImage(imageView: ImageView) {
            with(imageView) {
                background = ContextCompat.getDrawable(imageView.context,
                    R.drawable.background_profile
                )
                setImageResource(R.drawable.ic_person)
            }
        }
    }
}