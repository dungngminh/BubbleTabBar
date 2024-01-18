package io.ak1.parser

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.XmlResourceParser
import android.util.AttributeSet
import android.util.Xml
import androidx.annotation.MenuRes
import io.ak1.bubbletabbar.R
import org.xmlpull.v1.XmlPullParser.*

internal class MenuParser(private val context: Context) {

    private companion object {
        const val XML_MENU_TAG = "menu"
        const val XML_MENU_ITEM_TAG = "item"
    }

    fun parse(@MenuRes menuRes: Int): List<BubbleMenuItem> {
        @SuppressLint("ResourceType")
        val parser = context.resources.getLayout(menuRes)
        val attrs = Xml.asAttributeSet(parser)

        skipMenuTagStart(parser)

        return parseMenu(parser, attrs)
    }

    private fun skipMenuTagStart(parser: XmlResourceParser) {
        var currentEvent = parser.eventType
        do {
            if (currentEvent == START_TAG) {
                val name = parser.name
                require(name == XML_MENU_TAG) { "Expecting menu, got $name" }
                break
            }
            currentEvent = parser.next()
        } while (currentEvent != END_DOCUMENT)
    }

    private fun parseMenu(parser: XmlResourceParser, attrs: AttributeSet): List<BubbleMenuItem> {
        val items = mutableListOf<BubbleMenuItem>()
        var eventType = parser.eventType
        var isEndOfMenu = false

        while (!isEndOfMenu) {
            val name = parser.name
            when {
                eventType == START_TAG && name == XML_MENU_ITEM_TAG -> {
                    val item = parseMenuItem(attrs)
                    items.add(
                        item
                    )
                }

                eventType == END_TAG && name == XML_MENU_TAG -> isEndOfMenu = true
                eventType == END_DOCUMENT -> throw NullPointerException("Unexpected end of document")

            }
            eventType = parser.next()
        }
        return items
    }

    private fun parseMenuItem(attrs: AttributeSet): BubbleMenuItem {
        val sAttr = context.obtainStyledAttributes(attrs, R.styleable.Bubble)

        val item = BubbleMenuItem(
            id = sAttr.getResourceId(R.styleable.Bubble_android_id, 0),
            title = sAttr.getText(R.styleable.Bubble_android_title),
            icon = sAttr.getResourceId(R.styleable.Bubble_android_icon, 0),
            enabled = sAttr.getBoolean(R.styleable.Bubble_android_enabled, true),
            checked = sAttr.getBoolean(R.styleable.Bubble_android_checked, false),
        )

        sAttr.recycle()
        return item
    }
}