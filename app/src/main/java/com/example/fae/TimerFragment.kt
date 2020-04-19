package com.example.fae

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.fragment_timer.*


/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {
    private var soundID: Int = 0
    private lateinit var soundPool: SoundPool
    private var i = 0F
    private var handler=object: Handler(){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createSoundPool()


        fab_start.setOnClickListener {
            startTimer()
        }
        fab_stop.setOnClickListener {
            stopTimer()

        }
    }
    override fun onPause() {
        stopTimer()
        super.onPause()

    }




    private val countDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            i++
            Progress.progress = (i / 30 * 100).toInt()
            textView_countdown.text = "${millisUntilFinished / 1000}s"
        }

        override fun onFinish() {
            Progress.progress = 100
            instructionText.text = getString(R.string.breath)
            countDownTimer3.start()
            handler.postDelayed({
                startTimer()
            }, 10000)
            textView_countdown.text = "2"
        }
    }
    private val countDownTimer2 = object : CountDownTimer(30000, 600) {
        override fun onTick(millisUntilFinished: Long) {
            soundPool.play(soundID, 1F, 1F, 1, 0, 1F)
        }

        override fun onFinish() {
        }
    }
    private val countDownTimer3 = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Progress.progress = (100 * (10000 - millisUntilFinished) / 10000).toInt()
        }

        override fun onFinish() {
            Progress.progress = 100
        }
    }

    private fun startTimer() {
        countDownTimer.start()
        countDownTimer2.start()
        i = 0F
        instructionText.text = getString(R.string.chest)
        fab_start.isEnabled = false
    }

    private fun stopTimer() {
        countDownTimer.cancel()
        countDownTimer2.cancel()
        countDownTimer3.cancel()
        handler.removeCallbacksAndMessages(null)
        fab_start.isEnabled = true
        Progress.progress = 0
        textView_countdown.text = "30s"
        instructionText.text = getString(R.string.chest)
    }

    private fun createSoundPool() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME).build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build()
        } else {
            SoundPool(2, AudioManager.STREAM_MUSIC, 0)

        }
        soundID = soundPool.load(activity, R.raw.beep, 1)
    }
}
