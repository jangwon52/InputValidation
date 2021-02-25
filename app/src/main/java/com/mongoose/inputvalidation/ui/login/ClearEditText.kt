package com.mongoose.inputvalidation.ui.login

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.mongoose.inputvalidation.R

/**
 *  ClearEditText.kt
 *
 *  Created by jangwon on 2021/02/25
 *  Copyright © 2020 Shinhan Bank. All rights reserved.
 */

class ClearEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private lateinit var clearDrawable: Drawable

    init {
        isFocusableInTouchMode = true

        val tempDrawable: Drawable? = ContextCompat.getDrawable(getContext(), R.drawable.input_btn_delete_nor)
        tempDrawable?.let {
            clearDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTintList(clearDrawable, hintTextColors)
            clearDrawable.setBounds(0,
                0,
                clearDrawable.intrinsicWidth,
                clearDrawable.intrinsicHeight)
        }

        setClearIconVisible(false)

        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus()
                hideSoftKeyboard()
                // Call onDone result here
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        clearDrawable.setVisible(visible, false)
        setCompoundDrawables(null, null, if (visible) clearDrawable else null, null)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused) {
            setClearIconVisible(text?.length ?: 0 > 0)
        } else {
            setClearIconVisible(false);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        if (clearDrawable.isVisible && x > width - paddingRight - clearDrawable.intrinsicWidth) {
            if (event.action == MotionEvent.ACTION_UP) {
                error = null
                text = null
            }
            return true
        }

        return super.onTouchEvent(event)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFocused) {
            setClearIconVisible(text?.length ?: 0 > 0)
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    private fun hideSoftKeyboard() {
        val windowToken = rootView?.windowToken
        windowToken?.let {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it, 0)
        }
    }
}
