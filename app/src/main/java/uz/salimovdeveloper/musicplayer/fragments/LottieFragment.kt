package uz.salimovdeveloper.musicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.salimovdeveloper.musicplayer.R
import uz.salimovdeveloper.musicplayer.databinding.FragmentListBinding
import uz.salimovdeveloper.musicplayer.databinding.FragmentLottieBinding

class LottieFragment : Fragment() {
    private lateinit var binding: FragmentLottieBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLottieBinding.inflate(layoutInflater)



        return binding.root
    }
}