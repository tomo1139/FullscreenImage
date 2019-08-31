package develop.gamma2278.fullscreenimage

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import develop.gamma2278.fullscreenimage.databinding.ActivityFullscreenImageBinding

class FullscreenImageActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityFullscreenImageBinding>(this, R.layout.activity_fullscreen_image) }
    private val imageUrl = "https://tips-school.jp/info_network/imgs/global_network.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setupStatusBar(true, Color.TRANSPARENT, false)
        FullScreenService.hideNavigationBar(window)
        Picasso.with(this).load(imageUrl).fit().centerInside()
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
            if (binding.toolBar.alpha == 1f) {
                binding.toolBar.animate().alphaBy(1f).alpha(0f).setDuration(200).withEndAction { binding.toolBar.visibility = View.GONE }.start()
            } else if (binding.toolBar.alpha == 0f) {
                binding.toolBar.visibility = View.VISIBLE
                binding.toolBar.animate().alphaBy(0f).alpha(1f).setDuration(200).start()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) FullScreenService.hideNavigationBar(window)
    }
}

