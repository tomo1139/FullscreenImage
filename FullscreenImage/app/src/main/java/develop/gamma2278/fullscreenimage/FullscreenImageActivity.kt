package develop.gamma2278.fullscreenimage

import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import develop.gamma2278.fullscreenimage.databinding.ActivityFullscreenImageBinding
import okhttp3.OkHttpClient
import java.lang.Exception

class FullscreenImageActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityFullscreenImageBinding>(this, R.layout.activity_fullscreen_image) }
    private val imageUrl = "https://sdo.gsfc.nasa.gov/assets/img/latest/latest_2048_HMIIC.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPicasso()
        this.setupStatusBar(true, Color.TRANSPARENT, false)
        binding.toolBar.translationY = getStatusBarHeight(this).toFloat()
        FullScreenService.showNavigatoinBar(window)
        val builder = Picasso.Builder(this)
        builder.listener { picasso, uri, exception ->
            Log.e("dbg", "uri: " + uri + ", exception: " + exception.message)
        }
        builder.build().load(imageUrl).fit().centerInside()
            .into(binding.photoView, object : Callback {
                override fun onSuccess() {
                    binding.errorMessage.visibility = View.GONE
                }

                override fun onError() {
                    binding.errorMessage.visibility = View.VISIBLE
                }
            })
        binding.backButton.setOnClickListener { finish() }
        binding.photoView.setOnClickListener {
            if (binding.toolBarContainer.alpha == 1f) {
                FullScreenService.hideNavigationBar(window)
                binding.toolBarContainer.animate().alphaBy(1f).alpha(0f).setDuration(400).withEndAction { binding.toolBarContainer.visibility = View.INVISIBLE }.start()
            } else if (binding.toolBarContainer.alpha == 0f) {
                FullScreenService.showNavigatoinBar(window)
                binding.toolBarContainer.visibility = View.VISIBLE
                binding.toolBarContainer.animate().alphaBy(0f).alpha(1f).setDuration(400).start()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //if (hasFocus) FullScreenService.hideNavigationBar(window)
    }

    private fun setupPicasso() {
        val picasso = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(OkHttpClient.Builder().build()))
            .build()
        try {
            Picasso.setSingletonInstance(picasso)
        } catch (ex: IllegalStateException) {
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}

