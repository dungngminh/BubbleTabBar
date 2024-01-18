package io.ak1.parser

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes

/**
 * [BubbleMenuItem] to hold navigation tab element
 */
data class BubbleMenuItem(
    val id: Int,
    val title: CharSequence,
    @DrawableRes val icon: Int,
    val enabled: Boolean = true,
    val checked: Boolean = false,
) {
    @FontRes
    var customFont: Int = 0
    var horizontalPadding: Float = 0f
    var verticalPadding: Float = 0f
    var gap: Float = 0f
    var iconSize: Float = 0f
    var titleSize: Float = 0f
    var cornerRadius: Float = 0f
    var disabledIconColor: Int = Color.GRAY
    var iconColor: Int = Color.WHITE
    var borderColor: Int = Color.WHITE
    var borderWidth: Float = 0f
    var containerColor: Int = Color.WHITE
}