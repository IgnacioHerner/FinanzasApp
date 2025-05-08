package com.ignaherner.finanzasapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedIntanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardAccounts.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_accountsFragment)
        }

        binding.cardRecurring.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_recurringFragment)
        }

        //binding.cardSavings.setOnClickListener {
            //findNavController().navigate(R.id.action_menuFragment_to_savingsFragment)
        //}

        binding.cardSummary.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_monthlySummaryFragment)
        }

        binding.cardCategory.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_categorySummaryFragment)
        }

        binding.cardCredit.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_creditFragment)
        }

        //binding.cardCalendar.setOnClickListener {
            //findNavController().navigate(R.id.action_menuFragment_to_calendarFragment)
        //}

        //binding.cardSettings.setOnClickListener {
            //findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



