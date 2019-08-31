package develop.gamma2278.fullscreenimage

import android.annotation.TargetApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity

@JvmOverloads
fun FragmentActivity.setupStatusBar(
    fullScreen: Boolean = false,
    statusBarColor: Int? = null,
    lightMode: Boolean = true
) {
    when (android.os.Build.VERSION.SDK_INT) {
        in (16..20) -> {
            //do nothing
        }
        in (21..22) -> {
            //このVERSIONではlight modeを使えないので、light mode前提で設定されているStatusBarColorは無視する
            @TargetApi(21)
            window?.statusBarColor = statusBarColor?.let {
                if (ColorUtils.calculateLuminance(it) <= 0.5) {
                    //黒よりの色が指定されたのでそのまま使う
                    it
                } else {
                    //白よりの色が指定されたので無視する
                    ResourcesCompat.getColor(resources, R.color.black, null)
                }
            } ?: ResourcesCompat.getColor(resources, R.color.black, null)

            window?.decorView?.let {
                @TargetApi(21)
                it.systemUiVisibility = it.systemUiVisibility.let {
                    //フルスクリーンを設定
                    if (fullScreen) it or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else it
                }
            }
        }
        else -> {
            @TargetApi(23)
            window?.statusBarColor = statusBarColor ?: android.graphics.Color.WHITE

            window?.decorView?.let {
                @TargetApi(23)
                it.systemUiVisibility = it.systemUiVisibility.let {
                    //ライトモードを設定
                    if (lightMode) it or android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else it
                }.let {
                    //フルスクリーンを設定
                    if (fullScreen) it or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else it
                }
            }
        }
    }
}

