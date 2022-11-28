package uz.salimovdeveloper.musicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.salimovdeveloper.musicplayer.R
import uz.salimovdeveloper.musicplayer.databinding.FragmentMusicBinding

class MusicFragment : Fragment() {
    private lateinit var binding: FragmentMusicBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicBinding.inflate(layoutInflater)

        return binding.root
    }
}