package com.example.shoppingapplication.Dialog

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.shoppingapplication.R

class NextDialog(private val reportName: String, private val reportDescription: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Set the layout for the dialog
        dialog.setContentView(R.layout.report_response)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(width, height)

        // Set the name and description fields with the data passed in
        val nameTextView = dialog.findViewById<TextView>(R.id.report_response_name)
        nameTextView.text = reportName

        val descriptionTextView = dialog.findViewById<TextView>(R.id.report_response_desc)
        descriptionTextView.text = reportDescription

        return dialog
    }
}