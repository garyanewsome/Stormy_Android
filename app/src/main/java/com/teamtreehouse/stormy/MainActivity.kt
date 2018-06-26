package com.teamtreehouse.stormy


import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.teamtreehouse.stormy.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity: AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private var currentWeather: CurrentWeather? = null

    private var iconImageView: ImageView? = null

    val latitude: Double = 40.31
    val longitude: Double = -79.38

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getForecast(latitude, longitude)
    }

    private fun getForecast(latitude: Double, longitude: Double) {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this@MainActivity,
                R.layout.activity_main)


        var darkSky: TextView = findViewById(R.id.darkSkyAttribution)
        darkSky.movementMethod = LinkMovementMethod.getInstance()

        iconImageView = findViewById(R.id.iconImageView)

        val apiKey = "your_api_key_here"

        val forecastURL: String = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude

        if (isNetworkAvailable()) {
            val client = OkHttpClient()

            val request: Request = Request.Builder().url(forecastURL).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            var jsonData: String = response.body()!!.string()

                            Log.v(TAG, jsonData)

                            currentWeather = getCurrentDetails(jsonData)

                            val displayWeather = CurrentWeather(
                                    currentWeather!!.locationLabel,
                                    currentWeather!!.icon,
                                    currentWeather!!.time,
                                    currentWeather!!.temperature,
                                    currentWeather!!.humidity,
                                    currentWeather!!.precipChance,
                                    currentWeather!!.summary,
                                    currentWeather!!.timezone
                            )

                            binding.weather = displayWeather

                            runOnUiThread(object: Runnable {
                                override fun run() {
                                    val drawable: Drawable = resources.getDrawable(displayWeather.getIconId())
                                    iconImageView!!.setImageDrawable(drawable)
                                }
                            })


                        } else {
                            alertUserAboutError()
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "IO Exception caught", e)
                    } catch (e: JSONException) {
                        Log.e(TAG, "JSON Exception caught", e)
                    }
                }
            })
        }
    }

    private fun getCurrentDetails(jsonData: String): CurrentWeather{
        val forecast = JSONObject(jsonData)

        val timezone: String = forecast.getString("timezone")

        val currently:JSONObject = forecast.getJSONObject("currently")

        val currentWeather = CurrentWeather()
        currentWeather.humidity = currently.getDouble(("humidity"))
        currentWeather.time = currently.getLong("time")
        currentWeather.icon = currently.getString("icon")
        currentWeather.locationLabel = "Latrobe, PA"
        currentWeather.precipChance = currently.getDouble("precipProbability")
        currentWeather.summary = currently.getString("summary")
        currentWeather.temperature = currently.getDouble("temperature")
        currentWeather.timezone = timezone

        return currentWeather
    }

    private fun isNetworkAvailable(): Boolean {
        val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = manager.getActiveNetworkInfo()

        var isAvailable = false

        if(networkInfo.isConnected) {
            isAvailable = true
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show()
        }

        return isAvailable
    }

    private fun alertUserAboutError() {
        val dialog = AlertDialogFragment()
        dialog.show(fragmentManager, "error_dialog")
    }

    fun refreshOnClick(view: View) {
        getForecast(latitude, longitude)
        Toast.makeText(this, "REFRESHING DATA!", Toast.LENGTH_LONG).show()
    }
}
