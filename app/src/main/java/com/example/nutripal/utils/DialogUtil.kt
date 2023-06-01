package com.example.nutripal.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.nutripal.R

object DialogUtil {



     fun showDialogSuccesError(mode:String,context: Context,msgError:String,msgSuccess:String) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog_success, null)
        val tvPertama = view.findViewById<TextView>(R.id.tvPertama)
        val tvKedua = view.findViewById<TextView>(R.id.tvKedua)
        val ivDialog = view.findViewById<ImageView>(R.id.ivDialog)
        val close = view.findViewById<Button>(R.id.btn_close)

        if (mode == "success") {
            val title = mode
            ivDialog.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.undraw_completed_03xt
                )
            )
            tvPertama.text = title
            tvKedua.text = msgSuccess
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            close.setOnClickListener {
                dia.dismiss()
            }
        } else if (mode == "success") {
            val title = mode
            ivDialog.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.undraw_page_not_found_re_e9o6
                )
            )
            tvPertama.text = title
            tvKedua.text = msgError
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            close.setOnClickListener {
                dia.dismiss()
            }
        }
    }
}