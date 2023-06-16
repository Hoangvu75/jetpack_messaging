package com.example.kotlinjetpack.view.video_call_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.const.NAME
import live.videosdk.rtc.android.Meeting
import live.videosdk.rtc.android.Participant
import live.videosdk.rtc.android.VideoSDK
import live.videosdk.rtc.android.listeners.MeetingEventListener

class MeetingActivity : AppCompatActivity() {
    // declare the variables we will be using to handle the meeting
    private var meeting: Meeting? = null
    private var micEnabled = true
    private var webcamEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        val token = intent.getStringExtra("token")
        val meetingId = intent.getStringExtra("meetingId")
        val participantName = NAME

        VideoSDK.config(token)
        meeting = VideoSDK.initMeeting(
            this@MeetingActivity, meetingId, participantName,
            micEnabled, webcamEnabled,null, null, false, null)

        meeting!!.addEventListener(meetingEventListener)

        meeting!!.join()

        setActionListeners()

        val rvParticipants = findViewById<RecyclerView>(R.id.rvParticipants)
        rvParticipants.layoutManager = LinearLayoutManager(this)
        rvParticipants.adapter = ParticipantAdapter(meeting!!)
    }

    private val meetingEventListener: MeetingEventListener = object : MeetingEventListener() {
        override fun onMeetingJoined() {
            Log.d("#meeting", "onMeetingJoined()")
        }

        override fun onMeetingLeft() {
            Log.d("#meeting", "onMeetingLeft()")
            meeting = null
            if (!isDestroyed) finish()
        }

        override fun onParticipantJoined(participant: Participant) {
            Toast.makeText(
                this@MeetingActivity, participant.displayName + " joined",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onParticipantLeft(participant: Participant) {
            Toast.makeText(
                this@MeetingActivity, participant.displayName + " left",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setActionListeners() {
        // toggle mic
        findViewById<View>(R.id.btnMic).setOnClickListener {
            if (micEnabled) {
                meeting!!.muteMic()
                Toast.makeText(this@MeetingActivity, "Mic Muted", Toast.LENGTH_SHORT).show()
            } else {
                meeting!!.unmuteMic()
                Toast.makeText(this@MeetingActivity, "Mic Enabled", Toast.LENGTH_SHORT).show()
            }
            micEnabled=!micEnabled
        }

        findViewById<View>(R.id.btnWebcam).setOnClickListener {
            if (webcamEnabled) {
                meeting!!.disableWebcam()
                Toast.makeText(this@MeetingActivity, "Webcam Disabled", Toast.LENGTH_SHORT).show()
            } else {
                meeting!!.enableWebcam()
                Toast.makeText(this@MeetingActivity, "Webcam Enabled", Toast.LENGTH_SHORT).show()
            }
            webcamEnabled=!webcamEnabled
        }

        findViewById<View>(R.id.btnLeave).setOnClickListener {
            meeting!!.leave()
        }
    }

}