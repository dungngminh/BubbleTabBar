package io.ak1

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.IdRes
import io.ak1.bubbletabbar.R
import io.ak1.delegate.OnBubbleClickListener
import io.ak1.delegate.OnBubbleReselectedClickListener
import io.ak1.parser.BubbleMenuItem
import io.ak1.parser.MenuParser

class BubbleTabBar : LinearLayout {
    private var onBubbleClickListener: OnBubbleClickListener? = null
    private var onBubbleReselectedClickListener: OnBubbleReselectedClickListener? = null
    private var disabledIconColorParam: Int = Color.GRAY
    private var horizontalPaddingParam: Float = 0F
    private var iconPaddingParam: Float = 0F
    private var verticalPaddingParam: Float = 0F
    private var iconSizeParam: Float = 0F
    private var titleSizeParam: Float = 0F
    private var cornerRadiusParam: Float = 0F
    private var customFontParam: Int = 0
    private var iconColorParam: Int = Color.WHITE
    private var tabBorderWidthParam: Float = 0F
    private var tabBorderColorParam: Int = Color.WHITE
    private var tabContainerColorParam: Int = Color.WHITE

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    var selectedPosition: Int = 0
        set(value) {
            field = value
            setSelected(value)
        }
    var menus: List<BubbleMenuItem> = emptyList()
        set(value) {
            field = value
            removeAllViews()
            value.forEach { it ->
                it.apply {
                    horizontalPadding = horizontalPaddingParam
                    verticalPadding = verticalPaddingParam
                    iconSize = iconSizeParam
                    gap = iconPaddingParam
                    customFont = customFontParam
                    disabledIconColor = disabledIconColorParam
                    titleSize = titleSizeParam
                    cornerRadius = cornerRadiusParam
                    borderColor = tabBorderColorParam
                    borderWidth = tabBorderWidthParam
                    containerColor = tabContainerColorParam
                }
                addView(Bubble(context, it).apply { bind() })
            }
            invalidate()
        }

    fun setOnBubbleClickListener(onBubbleClickListener: OnBubbleClickListener) {
        this.onBubbleClickListener = onBubbleClickListener
    }

    fun setOnBubbleClickListener(onBubbleClickListener: (id: Int) -> Unit) {
        this.onBubbleClickListener = object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                onBubbleClickListener(id)
            }
        }
    }

    fun setOnBubbleReselectedListener(onBubbleClickListener: OnBubbleClickListener) {
        this.onBubbleClickListener = onBubbleClickListener
    }

    fun setOnBubbleReselectedListener(onBubbleReselectedClickListener: (id: Int) -> Unit) {
        this.onBubbleReselectedClickListener = object : OnBubbleReselectedClickListener {
            override fun onBubbleReselectedClick(id: Int) {
                onBubbleReselectedClickListener(id)
            }
        }
    }

    fun setSelected(position: Int) {
        val it = (this@BubbleTabBar.getChildAt(position) as Bubble)
        val b = it.id
        if (it.isSelected) {
            onBubbleReselectedClickListener?.onBubbleReselectedClick(it.id)
            return
        }
        if (oldBubble?.id != b) {
            it.isSelected = !it.isSelected
            oldBubble?.isSelected = false
        }
        oldBubble = it
        onBubbleClickListener?.onBubbleClick(it.id)
    }

    fun setSelectedWithId(@IdRes id: Int, callListener: Boolean = true) {
        val it = this@BubbleTabBar.findViewById<Bubble>(id) ?: return
        val b = it.id
        if (it.isSelected) {
            onBubbleReselectedClickListener?.onBubbleReselectedClick(it.id)
            return
        }
        if (oldBubble != null && oldBubble!!.id != b) {
            it.isSelected = !it.isSelected
            oldBubble?.isSelected = false
        }
        oldBubble = it
        if (onBubbleClickListener != null && callListener) {
            onBubbleClickListener?.onBubbleClick(it.id)
        }
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        if (attrs != null) {
            val attributes =
                context.theme.obtainStyledAttributes(attrs, R.styleable.BubbleTabBar, 0, 0)
            try {
                val menuResource =
                    attributes.getResourceId(R.styleable.BubbleTabBar_menu, -1)
                disabledIconColorParam = attributes.getColor(
                    R.styleable.BubbleTabBar_disabled_icon_color,
                    Color.GRAY
                )
                customFontParam =
                    attributes.getResourceId(R.styleable.BubbleTabBar_custom_font, 0)

                iconPaddingParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_gap,
                    resources.getDimension(R.dimen.bubble_icon_padding)
                )
                horizontalPaddingParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_horizontal_padding,
                    resources.getDimension(R.dimen.bubble_horizontal_padding)
                )
                verticalPaddingParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_vertical_padding,
                    resources.getDimension(R.dimen.bubble_vertical_padding)
                )
                iconSizeParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_icon_size,
                    resources.getDimension(R.dimen.bubble_icon_size)
                )
                titleSizeParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_title_size,
                    resources.getDimension(R.dimen.bubble_icon_size)
                )
                cornerRadiusParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_tab_corner_radius,
                    resources.getDimension(R.dimen.bubble_corner_radius)
                )
                iconColorParam = attributes.getColor(
                    R.styleable.BubbleTabBar_icon_color,
                    Color.WHITE
                )
                tabBorderColorParam = attributes.getColor(
                    R.styleable.BubbleTabBar_border_color,
                    Color.WHITE
                )
                tabContainerColorParam = attributes.getColor(
                    R.styleable.BubbleTabBar_container_color,
                    Color.WHITE
                )
                tabBorderWidthParam = attributes.getDimension(
                    R.styleable.BubbleTabBar_border_width,
                    resources.getDimension(R.dimen.bubble_border_width)
                )
                if (menuResource >= 0) {
                    setMenuResource(menuResource)
                }
            } finally {
                attributes.recycle()
            }


        }
    }


    private var oldBubble: Bubble? = null

    private fun setMenuResource(menuResource: Int) {
        val menu = (MenuParser(context).parse(menuResource))
        removeAllViews()
        Log.d("BottomNavigationBar", "--> Menu size = ${menu.size}")
        menu.forEach { it ->
            if (it.id == 0) {
                throw ExceptionInInitializerError("Id is not added in menu item")
            }
            it.apply {
                horizontalPadding = horizontalPaddingParam
                verticalPadding = verticalPaddingParam
                iconSize = iconSizeParam
                gap = iconPaddingParam
                customFont = customFontParam
                disabledIconColor = disabledIconColorParam
                titleSize = titleSizeParam
                cornerRadius = cornerRadiusParam
                borderColor = tabBorderColorParam
                borderWidth = tabBorderWidthParam
                containerColor = tabContainerColorParam
            }
            addView(Bubble(context, it).apply { bind() })
        }
        invalidate()
    }

    private fun Bubble.bind() {
        if (this.item.checked) {
            this.isSelected = true
            oldBubble = this
        }
        setOnClickListener {
            val b = it.id
            if (it.isSelected) {
                onBubbleReselectedClickListener?.onBubbleReselectedClick(it.id)
                return@setOnClickListener
            }
            if (oldBubble?.id != b) {
                (it as Bubble).isSelected = !it.isSelected
                oldBubble?.isSelected = false
            }
            oldBubble = it as Bubble
            onBubbleClickListener?.onBubbleClick(it.id)
        }
    }
}
