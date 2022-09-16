package com.incubasys.formblok.common.ui.ui.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.MessageFragmentCallback
import com.incubasys.formblok.common.ui.MessageAction
import kotlinx.android.synthetic.main.message_fragment.*

private const val INTENT_RES_ID = "res_id"
private const val INTENT_HEADING = "heading"
private const val INTENT_SUB_HEADING = "sub_heading"
private const val INTENT_BUTTON_TEXT = "button_text"
private const val INTENT_BUTTON_ACTION = "action"

class MessageFragment : Fragment() {

    private lateinit var callback: MessageFragmentCallback
    private lateinit var viewModel: MessageViewModel

    private var resId: Int? = null
    private var heading: String? = null
    private var subHeading: String? = null
    private var buttonText: String? = null
    private var buttonMessageAction: MessageAction? = null

    companion object {
        fun newInstance(
            resId: Int,
            heading: String,
            subHeading: String,
            buttonText: String,
            buttonMessageAction: MessageAction
        ): MessageFragment =
            MessageFragment().apply {
                arguments = Bundle().apply {
                    putInt(INTENT_RES_ID, resId)
                    putString(INTENT_HEADING, heading)
                    putString(INTENT_SUB_HEADING, subHeading)
                    putString(INTENT_BUTTON_TEXT, buttonText)
                    putSerializable(INTENT_BUTTON_ACTION, buttonMessageAction)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resId = it.getInt(INTENT_RES_ID)
            heading = it.getString(INTENT_HEADING)
            subHeading = it.getString(INTENT_SUB_HEADING)
            buttonText = it.getString(INTENT_BUTTON_TEXT)
            buttonMessageAction = it.getSerializable(INTENT_BUTTON_ACTION) as MessageAction?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.message_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        resId?.let { ivMessageImage.setImageResource(it) }
        heading?.let { tvMessageHeading.text = it }
        subHeading?.let { tvMessageSubHeading.text = it }
        buttonText?.let { bMessage.text = it }

        bMessage.setOnClickListener {
            buttonMessageAction?.let {
                when (it) {
                    MessageAction.EMAIL -> callback.onMessageButtonClick(MessageAction.EMAIL)
                    MessageAction.CONTINUE -> callback.onMessageButtonClick(MessageAction.CONTINUE)
                    MessageAction.LOGIN -> callback.onMessageButtonClick(MessageAction.LOGIN)
                    MessageAction.LOGIN_FROM_SIGNUP->callback.onMessageButtonClick(MessageAction.LOGIN_FROM_SIGNUP)
                    MessageAction.PASSWORD_CHANGE->callback.onMessageButtonClick(MessageAction.PASSWORD_CHANGE)
                    MessageAction.PROJECT_CREATED->callback.onMessageButtonClick(MessageAction.PROJECT_CREATED)
                    else -> {

                    }
                }
            }
        }
    }
}


