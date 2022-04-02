package com.example.chatme.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chatme.R
import com.example.chatme.databinding.ActivityVedioCallBinding
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration

class VedioCallActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_ID = 7
    // Ask for Android device permissions at runtime.
    private val ALL_REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE
    )
    private var mEndCall = false
    private var mMuted = false
    private var remoteView: SurfaceView? = null
    private var localView: SurfaceView? = null
    private lateinit var rtcEngine: RtcEngine
    lateinit var binding:ActivityVedioCallBinding
     var mRtcEventHandler:IRtcEngineEventHandler?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVedioCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRtcEventHandler = object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Joined Channel Successfully", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
                runOnUiThread {
                    setupRemoteVideoView(uid)
                }
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                runOnUiThread {
                    onRemoteUserLeft()
                }
            }
        }


        if (checkSelfPermission(ALL_REQUESTED_PERMISSIONS[0], PERMISSION_REQUEST_ID) &&
            checkSelfPermission(ALL_REQUESTED_PERMISSIONS[1], PERMISSION_REQUEST_ID
            ) && checkSelfPermission(ALL_REQUESTED_PERMISSIONS[2], PERMISSION_REQUEST_ID)) {
            initAndJoinChannel()
        }
        initAndJoinChannel()
        binding.buttonCall.setOnClickListener{
            endCall()
            finish()
        }

        binding.buttonMute.setOnClickListener{
            mMuted = !mMuted
            rtcEngine.muteLocalAudioStream(mMuted)
            val res: Int = if (mMuted) {
                R.drawable.mute
            } else {
                R.drawable.microphone
            }

            binding.buttonMute.setImageResource(res)
        }

        binding.buttonSwitchCamera.setOnClickListener{
            rtcEngine.switchCamera()
        }


    }
    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, ALL_REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ID) {
            if (
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                grantResults[2] != PackageManager.PERMISSION_GRANTED
            ) {

                Toast.makeText(applicationContext, "Permissions needed", Toast.LENGTH_LONG).show()
                finish()
                return
            }
            // Here we continue only if all permissions are granted.
            initAndJoinChannel()
        }
    }

    private fun startCall() {
        initRtcEngine()
        setupLocalVideoView()
        setupVideoConfig()
        joinChannel()
    }
    private fun endCall() {
        mEndCall=true
        removeLocalVideo()
        removeRemoteVideo()
        leaveChannel()
    }
    private fun initAndJoinChannel() {
        initRtcEngine()
        setupVideoConfig()
        setupLocalVideoView()
        joinChannel()
    }
    private fun initRtcEngine() {
        try {
            rtcEngine = RtcEngine.create(baseContext, getString(R.string.app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.d("tt", "initRtcEngine: $e")
        }
    }

    private fun setupVideoConfig() {

        rtcEngine.enableVideo()
        // Set the video encoding profile.
        rtcEngine.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideoView() {
        localView = RtcEngine.CreateRendererView(baseContext)
        localView!!.setZOrderMediaOverlay(true)
        binding.localVideoView.addView(localView)
        rtcEngine.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
    }

    private fun setupRemoteVideoView(uid: Int) {
        Log.d("aho", "setupRemoteVideoView: $uid")
        //if (binding.remoteVideoView.childCount > 1) {
        //    return
     //   }
        remoteView = RtcEngine.CreateRendererView(baseContext)
        binding.remoteVideoView.addView(remoteView)
        rtcEngine.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_FILL, uid))
    }

    private fun joinChannel() {
        val token = getString(R.string.agora_token)
        // Join a channel with a token.
        rtcEngine.joinChannel(token, "ChannelOne", "Extra Optional Data", 0)
    }
    private fun leaveChannel() {
        rtcEngine.leaveChannel()
    }

    private fun removeRemoteVideo() {
        if (remoteView != null) {
            binding.remoteVideoView.removeView(remoteView)
        }
        remoteView = null
    }

    private fun removeLocalVideo() {
        if (localView != null) {
            binding.localVideoView.removeView(localView)
        }
        localView = null
    }
    private fun onRemoteUserLeft() {
        removeRemoteVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
    }
}
