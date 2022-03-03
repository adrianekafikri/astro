package com.astro.test.adrian.util

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astro.test.adrian.Constant
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by adrianekafikri on 03/03/22.
 */
fun View.goneView() {
    this.visibility = View.GONE
}

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun Throwable.catchError() {
    when (this) {
        is HttpException -> {
            when {
                code() in 500..600 -> {
                    throw Throwable(Constant.SERVER_ERROR)
                }
                code() == 403 || code() == 401 -> {
                    throw Throwable(Constant.AUTHENTICATION_ERROR)
                }
            }
        }
        is IOException -> {
            throw Throwable(Constant.NETWORK_ERROR)
        }
        else -> throw this
    }
}

fun EditText.setTextChangeListener(
    debounceTime: Long = 0,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    block: (CharSequence?) -> Unit
): TextWatcher {
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    val debounceTimeInMills = timeUnit.toMillis(debounceTime)
    val isUseDebounce = debounceTimeInMills > 0
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (isUseDebounce) {
                runnable?.let {
                    handler.removeCallbacks(it)
                }
                runnable = Runnable {
                    block(s)
                }
                runnable?.let {
                    handler.postDelayed(it, debounceTimeInMills)
                }
            } else {
                block(s)
            }
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}

fun RecyclerView.setEndlessScrollListener(
    block: () -> Unit
) {
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null

    val scrollListener = object : RecyclerView.OnScrollListener() {
        var loading = true

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            if (canScrollVertically(-1)) {
                with(layoutManager as LinearLayoutManager) {
                    val visibleItemCount = childCount
                    val totalItemCount = itemCount
                    val firstVisibleItem = findFirstVisibleItemPosition()

                    if (loading) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                            runnable?.let {
                                handler.removeCallbacks(it)
                            }
                            runnable = Runnable {
                                loading = false
                                block()
                                loading = true
                            }
                            postDelayed(runnable, 500)
                        }
                    }
                }
            }
        }
    }

    addOnScrollListener(scrollListener)
}