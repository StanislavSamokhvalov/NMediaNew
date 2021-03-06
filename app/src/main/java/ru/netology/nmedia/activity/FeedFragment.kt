package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModelAuth: AuthViewModel by viewModels()

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                if (viewModelAuth.authenticated) {
                    if (!post.likedByMe) viewModel.likeById(post.id) else viewModel.unlikeById(post.id)
                } else findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onPicture(pic: String) {
                val bundle = Bundle().apply {
                    putString("pic", pic)
                }
                findNavController().navigate(
                    R.id.action_feedFragment_to_singleImageFragment,
                    bundle
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.errorGroup.isVisible = state.error
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.newerCount.observe(viewLifecycleOwner) { state ->
            if (state > 0) {
                viewModel.viewedCountSum += state
                binding.viewedButton.setText(getString(R.string.new_post, viewModel.viewedCountSum))
                binding.viewedButton.visibility = View.VISIBLE
            }
        }

        binding.viewedButton.setOnClickListener {
            viewModel.loadNewPosts()
            viewModel.viewedCountSum = 0
            binding.list.smoothScrollToPosition(0)
            binding.viewedButton.visibility = View.GONE
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshPosts()
            binding.viewedButton.visibility = View.GONE
        }

        binding.fab.setOnClickListener {
            if (viewModelAuth.authenticated) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else findNavController().navigate(R.id.action_feedFragment_to_signUpFragment)
        }
        return binding.root
    }
}