package com.example.nutripal.ui.userpreference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.nutripal.R

class CustomSpinerLevelActivity(context: Context, items: List<SpinerItemLevelActivity>) :
    ArrayAdapter<SpinerItemLevelActivity>(context,0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spiner_level_activity, parent, false)
        val text1 = view.findViewById<TextView>(R.id.text1)
        val text2 = view.findViewById<TextView>(R.id.text2)

        val item = getItem(position)
        val itemParts = item?.text?.split("|")
        if (itemParts?.size == 2) {
            text1.text = itemParts[0]
            text2.text = itemParts[1]
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
