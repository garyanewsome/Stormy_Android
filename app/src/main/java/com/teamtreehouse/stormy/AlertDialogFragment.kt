package com.teamtreehouse.stormy

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

class AlertDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.error_message))
                .setPositiveButton(getString(R.string.error_button_ok_text), null)

        return builder.create()
    }

}