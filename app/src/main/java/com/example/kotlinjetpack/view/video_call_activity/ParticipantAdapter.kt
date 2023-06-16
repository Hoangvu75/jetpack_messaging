package com.example.kotlinjetpack.view.video_call_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.function.AppSettings.getScreenHeight
import live.videosdk.rtc.android.Meeting
import live.videosdk.rtc.android.Participant
import live.videosdk.rtc.android.Stream
import live.videosdk.rtc.android.VideoView
import live.videosdk.rtc.android.listeners.MeetingEventListener
import live.videosdk.rtc.android.listeners.ParticipantEventListener
import org.webrtc.VideoTrack


class ParticipantAdapter(meeting: Meeting) : RecyclerView.Adapter<ParticipantAdapter.PeerViewHolder>() {

    private val participants: MutableList<Participant> = ArrayList()

    init {
        participants.add(meeting.localParticipant)

        meeting.addEventListener(object : MeetingEventListener() {
            override fun onParticipantJoined(participant: Participant) {
                participants.add(participant)
                notifyItemInserted(participants.size - 1)
            }

            override fun onParticipantLeft(participant: Participant) {
                var pos = -1
                for (i in participants.indices) {
                    if (participants[i].id == participant.id) {
                        pos = i
                        break
                    }
                }
                participants.remove(participant)
                if (pos >= 0) {
                    notifyItemRemoved(pos)
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        return PeerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_remote_peer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        val participant = participants[position]

        holder.tvName.text = participant.displayName

        for ((_, stream) in participant.streams) {
            if (stream.kind.equals("video", ignoreCase = true)) {
                holder.participantView.visibility = View.VISIBLE
                val videoTrack = stream.track as VideoTrack
                holder.participantView.addTrack(videoTrack)
                holder.progressBar.visibility = View.GONE
                break
            }
        }

        participant.addEventListener(object : ParticipantEventListener() {
            override fun onStreamEnabled(stream: Stream) {
                if (stream.kind.equals("video", ignoreCase = true)) {
                    holder.participantView.visibility = View.VISIBLE
                    val videoTrack = stream.track as VideoTrack
                    holder.participantView.addTrack(videoTrack)
                    holder.progressBar.visibility = View.GONE
                }
            }

            override fun onStreamDisabled(stream: Stream) {
                if (stream.kind.equals("video", ignoreCase = true)) {
                    holder.participantView.removeTrack()
                    holder.participantView.visibility = View.GONE
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    class PeerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var participantView: VideoView
        var tvName: TextView
        var progressBar: ProgressBar

        init {
            view.layoutParams.height = getScreenHeight() / 2

            progressBar = view.findViewById(R.id.circularProgressBar)
            tvName = view.findViewById(R.id.tvName)
            participantView = view.findViewById(R.id.participantView)
        }
    }
}

