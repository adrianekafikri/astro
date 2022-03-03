package com.astro.test.adrian.presentation

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by adrianekafikri on 03/03/22.
 */
data class GithubUserViewParam(
    val id: Int = 0,
    val avatar: String = "",
    val name: String = ""
) : DiffUtil.ItemCallback<GithubUserViewParam>() {
    override fun areItemsTheSame(oldItem: GithubUserViewParam, newItem: GithubUserViewParam): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GithubUserViewParam, newItem: GithubUserViewParam): Boolean {
        return listOf<Any>(oldItem.id, oldItem.avatar, oldItem.name) ==
                listOf<Any>(newItem.id, newItem.avatar, newItem.name)
    }

}