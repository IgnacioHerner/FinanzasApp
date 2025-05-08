package com.ignaherner.finanzasapp.ui.credit

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.data.CreditCardRepository
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentCreditBinding
import com.ignaherner.finanzasapp.model.CreditCardWithUsage
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModel
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModelFactory

class CreditFragment : Fragment() {

    private var _binding: FragmentCreditBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CreditAdapter

    private val viewModel: CreditCardViewModel by viewModels {
        CreditCardViewModelFactory(
            CreditCardRepository(AppDatabase.getDatabase(requireContext()).creditCardDao()),
            CreditTransactionRepository(AppDatabase.getDatabase(requireContext()).creditTransactionDao())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CreditAdapter(emptyList())
        binding.rvCreditCards.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCreditCards.adapter = adapter

        // Observar tarjetas con uso
        viewModel.cardsWithUsage.asLiveData().observe(viewLifecycleOwner) { list: List<CreditCardWithUsage> ->
            adapter.updateData(list)
        }

        // FAB para agregar nueva tarjeta
        binding.fabAddCard.setOnClickListener {
            val action = CreditFragmentDirections.actionCreditFragmentToAddEditCreditFragment(cardId = 0)
            findNavController().navigate(action)
        }

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val position = vh.adapterPosition
                val item = adapter.getItemAt(position)

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.delete(item.card)
                        Toast.makeText(requireContext(), "Tarjeta eliminada", Toast.LENGTH_SHORT).show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val action = CreditFragmentDirections.actionCreditFragmentToAddEditCreditFragment(item.card.id)
                        findNavController().navigate(action)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val item = vh.itemView
                val paint = Paint().apply { isAntiAlias = true }
                val iconSize = 48
                val iconMargin = 32
                val editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)
                val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)

                if (dX > 0) {
                    paint.color = Color.parseColor("#E3F2FD")
                    c.drawRoundRect(RectF(item.left.toFloat(), item.top.toFloat(), item.left + dX, item.bottom.toFloat()), 16f, 16f, paint)
                    editIcon?.setBounds(
                        item.left + iconMargin,
                        item.top + (item.height - iconSize) / 2,
                        item.left + iconMargin + iconSize,
                        item.top + (item.height + iconSize) / 2
                    )
                    editIcon?.draw(c)
                } else if (dX < 0) {
                    paint.color = Color.parseColor("#FFCDD2")
                    c.drawRoundRect(RectF(item.right + dX, item.top.toFloat(), item.right.toFloat(), item.bottom.toFloat()), 16f, 16f, paint)
                    deleteIcon?.setBounds(
                        item.right - iconMargin - iconSize,
                        item.top + (item.height - iconSize) / 2,
                        item.right - iconMargin,
                        item.top + (item.height + iconSize) / 2
                    )
                    deleteIcon?.draw(c)
                }

                super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
            }
        })

        touchHelper.attachToRecyclerView(binding.rvCreditCards)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

