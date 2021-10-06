package com.bookmygame.ui.groundOwner.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bookmygame.R
import com.bookmygame.databinding.FragmentAddSlotBinding
import com.bookmygame.ui.groundOwner.Slot
import com.bookmygame.util.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddSlotBottomSheet(
    private val action: Action,
    private val date: String,
    private var slot: Slot?
) : BottomSheetDialogFragment() {

    private var _binding: FragmentAddSlotBinding? = null
    private val binding get() = _binding!!
    private var listener: AddSlotListener? = null
//    private val slot = Slot(overs = "", time = "")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSlotBinding.inflate(inflater, container, false)
        if (slot == null) slot = Slot(
            id = "",
            noOfOvers = "",
            startTime = "",
            endTime = "",
            price = "",
            fromDate = date,
            toDate = date,
            ballType = ""
        )
        Log.d("Basava", "AddSlotBottomSheet onCreateView slot -> $slot")
        setUpViews()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpViews() {
        binding.apply {
            dropdownOvers.setSelection(
                (dropdownOvers.adapter as ArrayAdapter<String>).getPosition(
                    slot!!.noOfOvers
                )
            )
            dropdownOvers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("Basava", "Overs selected: selectedItem -> ${parent?.selectedItem}")
                    slot!!.noOfOvers = parent?.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("Basava", "Overs onNothingSelected")
                }
            }
            startTime.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Set Time")
                        .build()
                picker.show(childFragmentManager, picker.tag)

                picker.addOnPositiveButtonClickListener {
                    Log.d("Basava", "hour -> ${picker.hour}")
                    Log.d("Basava", "minute -> ${picker.minute}")

                    val timeBuilder =
                        StringBuilder().append(picker.hour).append(":").append(picker.minute)
                            .append(" ").append(if (picker.hour < 12) "AM" else "PM")
                    // TODO: 13/09/21 AM PM is not proper. Correct it
                    startTime.text = timeBuilder
                    slot!!.startTime = timeBuilder.toString()
                }
                picker.addOnNegativeButtonClickListener {

                }
                picker.addOnCancelListener {

                }
                picker.addOnDismissListener {

                }
            }

            dropdownBallType.setSelection(
                (dropdownBallType.adapter as ArrayAdapter<String>).getPosition(
                    slot!!.ballType
                )
            )
            dropdownBallType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("Basava", "Ball type: selectedItem -> ${parent?.selectedItem}")
                    slot!!.ballType = parent?.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d("Basava", "Ball type onNothingSelected")
                }
            }

            edittextPrice.setText(slot!!.price)

            addMoreDays.setOnClickListener {
                showDateRangeDialog()
            }

            addSlot.apply {
                text = when (action) {
                    // TODO: 13/09/21 Move these to strings.xml
                    Action.Add -> "Add"
                    Action.Update -> "Update"
                }
                setOnClickListener {
                    val price = edittextPrice.text.toString().trim()
                    slot!!.price = price
                    when (action) {
                        Action.Add -> listener?.onSlotAdded(slot!!)
                        Action.Update -> listener?.onSlotUpdated(slot!!)
                    }
                    dismiss()
                }
            }
        }
    }

    private fun showDateRangeDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar) // To show as dialog. Default is full screen

        // Restrict previous days from today
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val dateValidatorMin = DateValidatorPointForward.now()
        val listValidators = listOf(dateValidatorMin)
        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilderRange.setValidator(validators)
        builder.setCalendarConstraints(constraintsBuilderRange.build())

        val picker = builder.build()
        picker.show(childFragmentManager, picker.toString())

        picker.addOnNegativeButtonClickListener {
//            dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            Log.d(
                "Basava",
                "The selected date range is ${DateTimeUtil.millisecondToDate(it.first)} - ${
                    DateTimeUtil.millisecondToDate(it.second)
                }"
            )
        }
    }

    fun setOnAddSlotListener(listener: AddSlotListener) {
        this.listener = listener
    }

    interface AddSlotListener {
        fun onSlotAdded(slot: Slot)
        fun onSlotUpdated(slot: Slot)
    }

    enum class Action {
        Add, Update
    }
}