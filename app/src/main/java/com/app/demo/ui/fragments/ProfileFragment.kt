package com.app.demo.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.demo.ApplicationClass
import com.app.demo.R
import com.app.demo.adapters.FriendsAdapter
import com.app.demo.databinding.DialogAddNewFriendBinding
import com.app.demo.databinding.FragmentProfileBinding
import com.app.demo.interfaces.OnItemClickListener
import com.app.demo.model.Friends
import com.app.demo.ui.fragments.base.BaseFragment
import com.app.demo.viewModel.FriendsViewModel
import com.bumptech.glide.Glide


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var adapter: FriendsAdapter
    private val friendsViewModel: FriendsViewModel by viewModels {
        FriendsViewModel.FriendsViewModelFactory(ApplicationClass.getContext().repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, R.layout.fragment_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        listeners()
        setData()
    }


    private fun listeners() {

        ownerViewModel.allOwnerList.observe(viewLifecycleOwner) { owner ->
            if (owner.isEmpty()) {
                Glide.with(requireContext()).load(R.drawable.ic_owner).into(binding.imageView)
                return@observe
            }
            owner.lastOrNull()?.apply {
                binding.ownerName.text = name
                Glide.with(requireContext()).load(image)
                    .into(binding.imageView)
            }
        }
        binding.btnAddNewFriend.setOnClickListener {
            addNewFriendDialog()
        }
        binding.btnChangeProfileData.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    private fun initRecyclerView() {
        adapter = FriendsAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                showMessage("${adapter.currentList[position].name} is removed from friend list")
                friendsViewModel.deleteFriends(adapter.currentList[position])
            }

        })
    }

    private fun setData() {
        friendsViewModel.allFriendsList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }
    }

    private fun addNewFriendDialog() {
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        val dialogBinding = DialogAddNewFriendBinding.inflate(layoutInflater)

        dialogBinding.btnAdd.setOnClickListener {
            if (dialogBinding.etFriendName.text.isEmpty() || dialogBinding.etFriendAge.text.isEmpty()) {
                showMessage("Field is empty")
                return@setOnClickListener
            }
            try {
                val friend = Friends(
                    name = dialogBinding.etFriendName.text.toString(),
                    age = dialogBinding.etFriendAge.text.toString().toInt()
                )

                friendsViewModel.insertFriends(friend)
            } catch (e: Exception) {
                showMessage("friend not added make sure you added valid age.", Toast.LENGTH_LONG)
            }
            dialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }
}