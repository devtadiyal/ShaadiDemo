package com.example.medcords.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.medcords.R
import com.example.medcords.databinding.FragmentDetailsBinding
import com.example.medcords.utils.hide
import com.example.medcords.utils.show
import com.example.medcords.viewmodel.AuthViewModelFactory
import com.example.medcords.viewmodel.DetailsViewModel
import com.example.medcords.viewmodel.DetailsViewModelFactory
import com.example.medcords.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.list_photos.*
import kotlinx.android.synthetic.main.list_photos.image
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DetailsFragment : Fragment() , KodeinAware{

    private lateinit var detatilsViewModel: DetailsViewModel
    override val kodein by kodein()
    private val factory: DetailsViewModelFactory by instance()
    private lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details, container, false)
        detatilsViewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detatilsViewModel
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var details = DetailsFragmentArgs.fromBundle(requireArguments()).picDetails
        if(details!=null){
            //Glide handle image caching and image resizing by default
            Glide.with(requireContext()).load(details?.urls?.regular).into(locimage)
            placeName.setText("${details?.user?.location}")
            image_desc.setText("${details?.description+" "+details?.alt_description}")
            Glide.with(requireContext()).load(details?.user?.profile_image?.medium).into(userPic)
            name.setText("${details?.user?.name}")


            locimage.setOnClickListener {
                  //  Navigation.findNavController(it).navigate(R.id.action_detailsFragment_to_clientFragment)
                }

            }

    }
}