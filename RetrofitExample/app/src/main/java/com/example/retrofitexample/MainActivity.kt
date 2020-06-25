package com.example.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.retrofitexample.retrofit.RestfulAdapter
import com.example.retrofitexample.retrofit.domain.WeatherDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val API_KEY = "ea9463cc69d79542047ba01dae512a5e"

    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        okHttpButton.setOnClickListener { okHttpRetrofit() }

        rxButton.setOnClickListener { rxRetrofit() }
    }

    override fun onDestroy() {
        super.onDestroy()
        //mCompositeDisposable.clear()
        mCompositeDisposable.dispose()
    }

    private fun rxRetrofit() {
        val observable = RestfulAdapter.getServiceApi().getRxWeather(appid = API_KEY)

        mCompositeDisposable.add(
                    observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<WeatherDTO>() {
                        override fun onNext(weatherDTO: WeatherDTO) {
                            val name = weatherDTO.name
                            val temp = weatherDTO.main?.temp

                            nameTextView.text = name
                            tempTextView.text = temp.toString()
                        }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {

                    }
                })
        )
    }

    private fun okHttpRetrofit() {

        val call = RestfulAdapter.getServiceApi().getOkHttpWeather(appid = API_KEY)

        call.enqueue(object : Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful) {
                    val name = response.body()?.name
                    val temp = response.body()?.main?.temp

                    nameTextView.text = name
                    tempTextView.text = temp.toString()
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {

            }

        })
    }
}