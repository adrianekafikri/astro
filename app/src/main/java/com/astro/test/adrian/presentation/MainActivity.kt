package com.astro.test.adrian.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.astro.test.adrian.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.btnShowDialog.setOnClickListener {
            showUserDialog()
        }

        showUserDialog()
    }

    private fun showUserDialog() {
        try {
            supportFragmentManager.findFragmentByTag(GithubUserDialogFragment.TAG)?.let {
                supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
            }
            GithubUserDialogFragment.newInstance()
                .show(supportFragmentManager, GithubUserDialogFragment.TAG)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}