package com.example.nutripal.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.nutripal.R

object DialogUtil {

     @SuppressLint("CutPasteId")
     fun showDialogSuccesError(mode:String, context: Context, msgError:String, msgSuccess:String) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog_success, null)
        val tvPertama = view.findViewById<TextView>(R.id.tvPertama)
        val tvKedua = view.findViewById<TextView>(R.id.tvKedua)
        val ivDialog = view.findViewById<LottieAnimationView>(R.id.lottieAnim)
        val close = view.findViewById<Button>(R.id.btn_close)
         val animationView:LottieAnimationView = view.findViewById(R.id.lottieAnim);



        if (mode == "SUCCESS") {
            val title = mode

//            ivDialog.setAnimation(R.raw.success_animation)
//            ivDialog.resources.openRawResource(R.raw.success_animation)
            animationView.setAnimation(R.raw.success_animation);

            tvPertama.text = title
            tvKedua.text = msgSuccess
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            animationView.playAnimation();
            close.setOnClickListener {
                dia.dismiss()
            }
        } else if (mode == "ERROR") {
            val title = mode
//            ivDialog.setAnimation(R.raw.error_animation)
//            ivDialog.resources.openRawResource(R.raw.error_animation)
            animationView.setAnimation(R.raw.error_animation);

            tvPertama.text = title
            tvKedua.text = msgError
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            animationView.playAnimation();
            close.setOnClickListener {
                dia.dismiss()
            }
        }
    }
}