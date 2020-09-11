package net.along.fragonflyfm.util

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import fm.qingting.player.controller.PlaybackState
import fm.qingting.player.exception.PlaybackException
import fm.qingting.player.utils.parsePlaybackTime
import fm.qingting.qtsdk.QTException
import fm.qingting.qtsdk.player.QTPlayerManager
import fm.qingting.qtsdk.player.QTPlayer
import fm.qingting.qtsdk.player.listener.QTPlaybackListener
import kotlinx.android.synthetic.main.activity_player.*
import net.along.dragonflyfm.R
import net.along.dragonflyfm.base.BuildConfig
import java.net.URL

/**
 * 使用蜻蜓FM的原生API ： 直接获取电台的直播源  这是一个KotLin类
 * time : 2020.08.12
 */

class PlayerActivity : AppCompatActivity() {
    private var player: QTPlayer? = null
    private val playbackListener: QTPlaybackListener = object : QTPlaybackListener() {
        override fun onPrepareUrlFail() {
        }

        override fun onPlaybackStateChanged(playbackState: PlaybackState) {
            // 播放完成，自动播放下一集
            if (playbackState == PlaybackState.ENDED) {
                next()
            }
        }

        override fun onPlaybackProgressChanged(currentPositionMS: Long, bufferedPositionMS: Long, durationMS: Long) {
            activity_end_positions.text = parsePlaybackTime(durationMS)   //播放进度
            activity_track_seek_bar.max = durationMS.toInt()              //进度条
            activity_start_position.text = parsePlaybackTime(currentPositionMS) //播放时长
            if (!isSeeking) {
                activity_track_seek_bar.progress = currentPositionMS.toInt()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
        }
    }
    private var isSeeking = false       //播放状态
    private var channelId: Int? = null   //节目id
    private var programIds: ArrayList<Int>? = null  //节目数
    private var curIndex = 0   //节目单号

//    private var duration = 0  //节目长度

    private var channelName: String? = null //界面名称
    private var title: String? = null //电台名称
    private var username: String? = null //主播
    private var cover: String? = null //图片

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        //当Android版本高于4.0时，允许在主线程中下载小流量图片
        if (android.os.Build.VERSION.SDK_INT > 9) {
            var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        channelId = intent.getSerializableExtra("channelId") as? Int
        programIds = @Suppress("UNCHECKED_CAST") (intent.getSerializableExtra("programIds") as? ArrayList<Int>)
        curIndex = intent.getIntExtra("currentProgramIdIndex", 0)

        channelName = intent.getStringExtra("channelName")
        username = intent.getStringExtra("username")
        title = intent.getStringExtra("title")
        cover = intent.getStringExtra("cover")

        if (cover != null) {
            var myUrl = URL(cover.toString())    // 把传过来的路径转成url
            var conn = myUrl.openConnection()    // 获取链接
            conn.connect()                     // 开始连接
            var imaBit = conn.getInputStream()   // 得到从服务器端发回的数据
            var bmp = BitmapFactory.decodeStream(imaBit) // 使用工厂把网络的输入流生产Bitmap
            imaBit.close()               //关闭
            activity_player_radio_pictures.setImageBitmap(bmp)  //图片
        }
        activity_play_title.text = title  //电台名称
        activity_station_name.text = channelName  //地方电台名称
        activity_player_anchor.text = username    //主播
//        activity_end_positions.text = duration.toString()

        //点击返回上一页
        activity_player_return.setOnClickListener {
            finish()
        }
        activity_station_name.setOnClickListener {
            finish()
        }

        QTPlayerManager.obtainPlayer(object : QTPlayerManager.Connect2PlayerCallback {
            override fun onConnected(player: QTPlayer) {
                this@PlayerActivity.player = player.also {
                    it.addPlaybackListener(playbackListener)
                    if (BuildConfig.DEBUG) {
                        it.startDebug()
                    }
                }
                prepare2Play()
            }

            override fun onDisconnected() {
                player = null
            }

            override fun onFair(e: QTException) {
                e.printStackTrace()
                player = null
            }
        })

        activity_track_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isSeeking = false
                player?.seekTo(seekBar.progress)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 这里的列表自动播放都是通过programIds和播放回调状态为ended来控制的，会导致列表播放失效
        player?.removePlaybackListener(playbackListener)
        player = null
//        // 停止播放器服务，同时会释放播放器资源
//        // 使用场景不需要播放器进行工作
//        QTPlayerManager.release()
    }

    fun onClickPlayControl(v: View) {
        when (v) {
            activity_play_rew -> player?.rewind()              //后退
            activity_play_next -> player?.fastForward()        //快进
//            activity_play_rew -> previous()
//            activity_play_next -> next()
            activity_play_play -> play()                       //播放
            activity_play_pause -> pause()                     //暂停

        }
    }

    private fun prepare2Play() =
            channelId?.let {
                if (programIds != null) {
                    programIds?.getOrNull(curIndex)
                            ?.apply {
                                player?.prepare(it, this)
                            }
                } else {
                    player?.prepare(it)
                }
            }

    /**
     * 播放
     */
    private fun play() {
        when (player?.playbackState) {
            PlaybackState.PLAYING, PlaybackState.PAUSE, PlaybackState.ENDED -> player?.play()
            else -> prepare2Play()
        }
    }

    /**
     * 暂停
     */
    private fun pause() {
        player?.pause()
    }

    /**
     * 下一首 ： 用于听书使用，现在暂时不使用
     */
    private fun next() {
        programIds?.takeIf {
            it.isNotEmpty() && curIndex < it.size - 1
        }?.let {
            ++curIndex
            prepare2Play()
        }
    }

    /**
     * 上一首 ： 用于听书使用，现在暂时不使用
     */
    private fun previous() {
        programIds?.takeIf {
            it.isNotEmpty() && curIndex > 0
        }?.let {
            --curIndex
            prepare2Play()
        }
    }

    companion object {
        //播放专辑节目需要专辑id跟节目ID，如果播放的是广播，只需要channelId programIs为空就好
        @JvmOverloads
        fun start(context: Context, channelId: Int, programIds: ArrayList<Int>?, currentProgramIdIndex: Int? = 0,
                  channelName: String, username: String, title: String, cover: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("channelId", channelId)
            if (programIds != null && programIds.size != 0) {
                intent.putExtra("programIds", programIds)
                intent.putExtra("currentProgramIdIndex", currentProgramIdIndex)
            }
            intent.putExtra("channelName", channelName)
            intent.putExtra("username", username)
            intent.putExtra("title", title)
            intent.putExtra("cover", cover)
            context.startActivity(intent)
        }
    }
}
