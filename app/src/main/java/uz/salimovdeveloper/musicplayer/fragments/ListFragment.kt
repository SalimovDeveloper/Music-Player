package uz.salimovdeveloper.musicplayer.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.salimovdeveloper.musicplayer.R
import uz.salimovdeveloper.musicplayer.adapters.RvAdapter
import uz.salimovdeveloper.musicplayer.databinding.FragmentListBinding
import uz.salimovdeveloper.musicplayer.models.Music

class ListFragment : Fragment(), RvAdapter.RvClick {
    private lateinit var binding: FragmentListBinding
    private lateinit var rvAdapter: RvAdapter
    private var REQUEST_PERMISSSION: Int = 99
    private lateinit var list: ArrayList<Music>
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        var musicList = ArrayList<Music>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        musicList.addAll(requireContext().getAllAudio())
        MyData.musicList.addAll(requireContext().getAllAudio())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onClick(label: Music) {

    }



    private fun getPausePlay() {
        if (MyData.path != null && MyData.path!!.isPlaying) {
            binding.playPauseImage.setImageResource(R.drawable.pause)
            binding.playerSongName.text = musicList[MyData.index].title
            binding.playerSongAuthor.text = musicList[MyData.index].author
        }
    }

    private fun nextButton() {
        binding.playNext.setOnClickListener {
            if (MyData.path != null) {
                mediaPlayer!!.stop()
                index = ++index
                if (index >= musicList.size) index = 0
                mediaPlayer =
                    MediaPlayer.create(binding.root.context, Uri.parse(musicList[index].musicPath))
                MyData.path = mediaPlayer
                binding.playerSongName.text = musicList[index].title
                binding.playerSongAuthor.text = musicList[index].author
                binding.playPauseImage.setImageResource(R.drawable.pause)
                mediaPlayer!!.start()
            }
        }
    }

    private fun lastButton() {
        binding.playBack.setOnClickListener {
            if (MyData.path != null) {
                mediaPlayer!!.stop()
                if (index < 0) index = musicList.size
                index = --index
                mediaPlayer =
                    MediaPlayer.create(binding.root.context, Uri.parse(musicList[index].musicPath))
                MyData.path = mediaPlayer
                binding.playerSongName.text = musicList[index].title
                binding.playerSongAuthor.text = musicList[index].author
                binding.playPauseImage.setImageResource(R.drawable.pause)
                mediaPlayer!!.start()
            }
        }
    }

    private fun pausePlayButton() {
        binding.playPause.setOnClickListener {
            if (MyData.path != null) {
                if (MyData.path!!.isPlaying) {
                    binding.playPauseImage.setImageResource(R.drawable.play_button)
                    MyData.path!!.pause()
                } else {
                    binding.playPauseImage.setImageResource(R.drawable.pause)
                    MyData.path!!.start()
                }
            }
        }
    }

    private fun openPlayer() {
        binding.player.setOnClickListener {
            if (MyData.path != null) {
                val navOption = NavOptions.Builder()
                navOption.setEnterAnim(R.anim.open_player_animation)
                navOption.setPopEnterAnim(R.anim.open_player_pop_animation)
                navOption.setExitAnim(R.anim.exit_player_animation)
                navOption.setPopExitAnim(R.anim.exit_player_pop_animation)
                findNavController().navigate(R.id.playMusicFragment, bundleOf(), navOption.build())
                MyData.index = index
            }
        }
    }


    @SuppressLint("Range", "Recycle")
    fun Context.getAllAudio(): java.util.ArrayList<Music> {
        val tempList = java.util.ArrayList<Music>()
        //get the external storage media storage audio uri
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //IS_MUSIC : None-zero if the audio file is music
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        //sort music
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        //Query for external storage for music files
        val cursor: Cursor? = this.contentResolver.query(
            uri, // Uri
            null, // Projection
            selection, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val imageId: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
            val authorId: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)

            do {
                val audioId: Long = cursor.getLong(id)
                val audioTitle: String = cursor.getString(title)
                var imagePath: String = ""
                if (imageId != -1) {
                    imagePath = cursor.getString(imageId)
                } else {
                    imagePath = R.drawable.ic_launcher_background.toString()
                }
                val musicPath: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val artist = cursor.getString(authorId)

                // Add the current music to the list
                tempList.add(Music(audioId, audioTitle, imagePath, musicPath, artist))
            } while (cursor.moveToNext())
        }




        return tempList
    }


    override fun onClick(music: Music) {
        if (MyData.path != null) {
            MyData.path!!.stop()
            mediaPlayer = MediaPlayer.create(binding.root.context, Uri.parse(music.musicPath))
            MyData.path = mediaPlayer
            index = musicList.indexOf(music)
            binding.playPauseImage.setImageResource(R.drawable.pause)
            binding.playerSongName.text = music.title
            binding.playerSongAuthor.text = music.author
            mediaPlayer!!.start()
        } else {
            mediaPlayer = MediaPlayer.create(binding.root.context, Uri.parse(music.musicPath))
            MyData.path = mediaPlayer
            index = musicList.indexOf(music)
            binding.playerSongName.text = music.title
            binding.playerSongAuthor.text = music.author
            binding.playPauseImage.setImageResource(R.drawable.pause)
            mediaPlayer!!.start()
        }
    }


    private fun previousMusic(){
        if (mediaPlayer!=null){
            mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
                @SuppressLint("SetTextI18n")
                override fun onCompletion(mp: MediaPlayer?) {
                    mediaPlayer!!.stop()
                    ++index
                    if (index >= musicList.size) index = 0
                    mediaPlayer=MediaPlayer.create(binding.root.context, Uri.parse(musicList[index].musicPath))
                    mediaPlayer!!.start()
                    MyData.path=mediaPlayer
                    binding.playerSongName.text=musicList[index].title
                    binding.playerSongAuthor.text=musicList[index].author
                    MyData.index=index
                    MyData.path=mediaPlayer
                }
            })
        }
    }


    private fun chekAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(binding.root.context, "Audio permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            requestAudioPermissions()
        }
    }

    private fun requestAudioPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            val builder = AlertDialog.Builder(binding.root.context)
            builder.setMessage("Ovoz yozib olish uchun ruxsat berishingiz kerak aks holda ilova irofonni ishlata olmaydi")
            builder.setTitle("Permissions")

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSSION
                )
            })
            builder.create().show()

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSSION
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.REQUEST_PERMISSSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicList.addAll(requireContext().getAllAudio())
                MyData.musicList.addAll(requireContext().getAllAudio())
                myRvAdapter.list = musicList
                myRvAdapter.notifyDataSetChanged()
            }
        }
    }
}