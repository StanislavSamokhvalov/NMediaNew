package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.viewmodel.SignInViewModel

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel.data.observe(viewLifecycleOwner, {
            AppAuth.getInstance().setAuth(it.id, it.token)
            findNavController().popBackStack()
        })

        with(binding) {
            login.requestFocus()
            login.setOnClickListener {
                viewModel.attemptLogin(
                    login.text.toString(),
                    password.text.toString())
            }
        }
        return binding.root
    }
}
