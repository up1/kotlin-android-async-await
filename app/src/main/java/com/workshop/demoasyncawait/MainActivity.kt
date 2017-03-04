package com.workshop.demoasyncawait

import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import co.metalab.asyncawait.async
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        solutionWithAsyncAwait()
    }

    fun solutionWithAsyncAwait(){
        async {
            try {
                val user = await {
                    UserService.getById(1)
                }
                username.text = "Username = $user"
            }catch (e: IOException){
                username.text = "Can not get username from service"
            }
        }.onError {
            username.text = "Can not handle error !!"
        }.finally {
            username.setTextColor(Color.GREEN)
        }
        username.text = "Waiting ..."
    }

    fun solutionWithAsynTask() {
        object: AsyncTask<Unit, Unit, String?>() {

            override fun doInBackground(vararg params: Unit?): String? {
                try{
                    val user = UserService.getById(1)
                    return user
                }catch (e: IOException){
                    return null
                }
            }

            override fun onPostExecute(result: String?) {
                if (result!= null) {
                    username.text = "Username = $result"
                }else {
                    username.text = "Can not get username from service"
                }
            }

        }.execute()
    }

    fun solutionWithThread() {
        val handler = Handler()
        Thread{
            try {
                val user = UserService.getById(1)
                handler.post {
                    username.text = "Username = $user"
                }
            }catch(e: IOException) {
                handler.post {
                    username.text = "Can not get username from service"
                }
            }
        }.start()
    }
}

object UserService {
    fun getById(id: Int): String {
        Thread.sleep(3000)
        return "My username"
    }

}

