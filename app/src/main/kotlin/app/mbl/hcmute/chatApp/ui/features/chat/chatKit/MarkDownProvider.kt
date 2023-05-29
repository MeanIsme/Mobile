package app.mbl.hcmute.chatApp.ui.features.chat.chatKit

import android.content.Context
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin

class MarkDownProvider {
    companion object {
        var instance: Markwon? = null

        fun initMarkDown(context: Context) {
            if (instance == null) instance = Markwon.builder(context)
                // create default instance of TablePlugin
//                .usePlugin(TablePlugin.create(context))
                .build()
        }

        fun clear() {
            instance = null
        }
    }
}