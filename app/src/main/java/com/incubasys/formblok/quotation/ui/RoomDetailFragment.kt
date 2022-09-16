package com.incubasys.formblok.quotation.ui


import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.ui.superscript
import com.incubasys.formblok.databinding.FragmentRoomDetailBinding
import com.incubasys.formblok.quotation.viewmodel.CreateQuotationViewModel
import kotlinx.android.synthetic.main.fragment_room_detail.*
import java.text.DecimalFormat


class RoomDetailFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRoomDetailBinding
    private lateinit var viewModel: CreateQuotationViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from((bottomSheet as FrameLayout)).state =
                BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as CreateQuotationActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_room_detail, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view!!.parent as View).setBackgroundColor(Color.TRANSPARENT)

        bRoomDetailConfirm.setOnClickListener {
            if (viewModel.customCalculatedAreaToShow.value!! <= viewModel.propertyOutput.total_liveable_area) {
                tvCalculatedAreaValue.setTextColor(ContextCompat.getColor(tvCalculatedAreaValue.context, android.R.color.black))
                viewModel.room.value?.width = viewModel.customWidth.value!!
                viewModel.room.value?.length = viewModel.customLength.value!!
                viewModel.calculatedAreaToShow.value = viewModel.customCalculatedAreaToShow.value
                viewModel.updateItem(viewModel.roomPosition.value!!, false)
                dismiss()
            } else {
                tvCalculatedAreaValue.setTextColor(ContextCompat.getColor(tvCalculatedAreaValue.context, android.R.color.holo_red_light))
                Toast.makeText(activity?.applicationContext, "Your calculated area should be less than livable area", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        bRoomDetailCancel.setOnClickListener {
            dismiss()
        }

        etWidth.setOnFocusChangeListener { _, b ->
            if (b) {
                if (etWidth.text.contains("m2")) {
                    etWidth.text = etWidth.text.delete(etWidth.length() - 2, etWidth.length())
                }
            } else {
                etWidth.text = SpannableStringBuilder().apply {
                    append(etWidth.text)
                    append(superscript("m2"))
                }
            }
        }
        etLength.setOnFocusChangeListener { _, b ->
            if (b) {
                if (etLength.text.contains("m2")) {
                    etLength.text = etLength.text.delete(etLength.length() - 2, etLength.length())
                }
            } else {
                etLength.text = SpannableStringBuilder().apply {
                    append(etLength.text)
                    append(superscript("m2"))
                }
            }
        }

        etWidth.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etWidth.clearFocus()
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        etLength.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etWidth.clearFocus()
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.customWidth.value = viewModel.room.value?.width
        viewModel.customLength.value = viewModel.room.value?.length
        viewModel.actualRoomArea = viewModel.room.value?.width?.times(viewModel.room.value?.length!!)!!
        viewModel.customArea.value = viewModel.room.value?.width?.times(viewModel.room.value?.length!!)!!
        viewModel.customCalculatedAreaToShow.value = viewModel.calculatedAreaToShow.value

        viewModel.customWidth.observe(this, Observer {
            viewModel.customArea.value = it.times(viewModel.customLength.value!!)
            etArea.text = SpannableStringBuilder().apply {
                append(DecimalFormat("##.##").format(viewModel.customArea.value!!))
                append(superscript("m2"))
            }
            tvCalculatedAreaValue.text = SpannableStringBuilder().apply {
                append(DecimalFormat("##.##").format(viewModel.customCalculatedAreaToShow.value))
                append(superscript("m2"))
            }
        })

        viewModel.customLength.observe(this, Observer {
            viewModel.customArea.value = it.times(viewModel.customWidth.value!!)
            etArea.text = SpannableStringBuilder().apply {
                append(DecimalFormat("##.##").format(viewModel.customArea.value!!))
                append(superscript("m2"))
            }
            tvCalculatedAreaValue.text = SpannableStringBuilder().apply {
                append(DecimalFormat("##.##").format(viewModel.customCalculatedAreaToShow.value))
                append(superscript("m2"))
            }
        })

        var newLiveableArea: Double
        viewModel.customArea.observe(this, Observer {
            when {
                it > viewModel.actualRoomArea -> {
                    viewModel.customCalculatedAreaToShow.value = viewModel.calculatedAreaToShow.value
                    newLiveableArea = it.minus(viewModel.actualRoomArea)
                    viewModel.customCalculatedAreaToShow.value =
                        viewModel.customCalculatedAreaToShow.value?.plus(newLiveableArea)

                    tvCalculatedAreaValue.text = SpannableStringBuilder().apply {
                        append(DecimalFormat("##.##").format(viewModel.customCalculatedAreaToShow.value))
                        append(superscript("m2"))
                    }
                }
                it < viewModel.actualRoomArea -> {
                    viewModel.customCalculatedAreaToShow.value = viewModel.calculatedAreaToShow.value
                    newLiveableArea = viewModel.actualRoomArea.minus(it)
                    viewModel.customCalculatedAreaToShow.value =
                        viewModel.customCalculatedAreaToShow.value?.minus(newLiveableArea)

                    tvCalculatedAreaValue.text = SpannableStringBuilder().apply {
                        append(DecimalFormat("##.##").format(viewModel.customCalculatedAreaToShow.value))
                        append(superscript("m2"))
                    }
                }
                it == viewModel.actualRoomArea -> {
                    viewModel.customCalculatedAreaToShow.value = viewModel.calculatedAreaToShow.value
                    tvCalculatedAreaValue.text = SpannableStringBuilder().apply {
                        append(DecimalFormat("##.##").format(viewModel.customCalculatedAreaToShow.value!!))
                        append(superscript("m2"))
                    }
                }
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RoomDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
