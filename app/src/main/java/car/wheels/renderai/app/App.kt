package car.wheels.renderai.app

import android.app.Application
import android.util.Log
import com.example.simgplechatexample.data.db.ChatDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {


    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "App is created")

        initChatDeps()
    }

    private fun initChatDeps() {
        ChatDatabase.init(this)
    }

    companion object {

        const val TAG = "MyApplication"
    }
}
