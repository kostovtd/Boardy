package com.kostovtd.herorealms.views.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kostovtd.herorealms.R

class SetUpHeroRealmsModeFragment : Fragment(R.layout.fragment_hero_realms_mode) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.oneVSone).setOnClickListener {
            findNavController().navigate(R.id.action_setUpHeroRealmsModeFragment_to_setUpHeroRealmsPlayersFragment)
        }
    }
}