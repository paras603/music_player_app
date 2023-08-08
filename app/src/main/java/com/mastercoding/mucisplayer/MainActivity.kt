package com.mastercoding.mucisplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var time_text: TextView
    lateinit var seek_bar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var play_btn = findViewById<Button>(R.id.playBtn)
        val stop_btn : Button = findViewById(R.id.pauseBtn)
        val forward_btn: Button = findViewById(R.id.forwardBtn)
        val back_btn : Button = findViewById(R.id.rewindBtn)

        var song_title = findViewById<TextView>(R.id.songTitle)
        time_text = findViewById<TextView>(R.id.time_left_text)

        seek_bar = findViewById<SeekBar>(R.id.seekBar)

        //Media Player
        var mediaPlayer = MediaPlayer.create(this,
                                        R.raw.manka_kura)

        seek_bar.isClickable = false

        //adding functionalities to button
        play_btn.setOnClickListener(){
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()


            if(oneTimeOnly == 0){
                seek_bar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_text.text = startTime.toString()
            seek_bar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime,100)
        }

        // Setting the music title
        song_title.text = ""+ resources.getResourceEntryName(R.raw.manka_kura)

        // pause Button
        stop_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        // Forward Button
        forward_btn.setOnClickListener(){
            var temp = startTime
            if ((temp + forwardTime) <= finalTime){
                startTime = startTime + forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this,
                    "Can't Jump forward", Toast.LENGTH_LONG).show()
            }
        }

        back_btn.setOnClickListener(){
            var temp = startTime.toInt()

            if ((temp - backwardTime) >0){
                startTime = startTime - backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this,
                    "Can't Jump backward",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    //Creating the Runnable
    val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_text.text = "" +
                    String.format(
                        "%d min , %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            ))
                    )


            seek_bar.progress = startTime.toInt()
            handler.postDelayed(this, 100)

        }
    }

}