package kr.book_stack

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import kr.book_stack.databinding.DialogPopupBinding
import kr.book_stack.fragment_reg.HFragment3
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.richtext.RichTextList
import java.nio.ByteBuffer
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object MyUtil{

    fun dialogCloseTypeView(context: Context, inTitle:String, inMsg: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(inTitle)
        builder.setMessage(inMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("닫기") { _, _ ->
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }



}