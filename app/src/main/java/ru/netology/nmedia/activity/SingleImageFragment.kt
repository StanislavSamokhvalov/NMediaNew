package ru.netology.nmedia.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSingleImageBinding

class SingleImageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //change color
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            val statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.colorDarkBackground)
            activity.window.statusBarColor = statusBarColor
            activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(statusBarColor))
        }

        val binding = FragmentSingleImageBinding.inflate(inflater, container, false)

        val url = arguments?.getString("pic")

        binding.apply {
            picture.visibility = View.GONE
            url?.let {
                val urlPicture = "${BuildConfig.BASE_URL}/media/${it}"
                Glide.with(picture)
                    .load(urlPicture)
                    .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                    .timeout(10000)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(picture)
            }

            picture.visibility = View.VISIBLE
        }

        binding.picture.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //return color
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            val statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.design_default_color_primary_variant
            )
            activity.window.statusBarColor = statusBarColor
            activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(statusBarColor))
        }
    }
}