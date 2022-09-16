package com.incubasys.formblok.common.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.MessageFragmentCallback
import com.incubasys.formblok.common.extenstions.changeStatusBarColorYellow
import com.incubasys.formblok.common.extenstions.replaceFragment
import com.incubasys.formblok.common.ui.ui.message.MessageFragment
import com.incubasys.formblok.home.ui.HomeActivity
import kotlinx.android.synthetic.main.message_activity.*

private const val INTENT_RES_ID = "res_id"
private const val INTENT_HEADING = "heading"
private const val INTENT_SUB_HEADING = "sub_heading"
private const val INTENT_BUTTON_TEXT = "button_text"
private const val INTENT_BUTTON_ACTION = "action"

class MessageActivity : BaseActivity(), MessageFragmentCallback {

    companion object {
        fun newIntent(
            context: Context,
            resId: Int,
            heading: String,
            subHeading: String,
            buttonText: String,
            buttonMessageAction: MessageAction
        ): Intent =
            Intent(context, MessageActivity::class.java).apply {
                putExtra(INTENT_RES_ID, resId)
                putExtra(INTENT_HEADING, heading)
                putExtra(INTENT_SUB_HEADING, subHeading)
                putExtra(INTENT_BUTTON_TEXT, buttonText)
                putExtra(INTENT_BUTTON_ACTION, buttonMessageAction)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColorYellow(this)
        setContentView(R.layout.message_activity)
        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        replaceFragment(
            MessageFragment.newInstance(
                intent.getIntExtra(INTENT_RES_ID, -1),
                intent.getStringExtra(INTENT_HEADING),
                intent.getStringExtra(INTENT_SUB_HEADING),
                intent.getStringExtra(INTENT_BUTTON_TEXT),
                intent.getSerializableExtra(INTENT_BUTTON_ACTION) as MessageAction
            ),
            flMessageContainer.id, animation = false
        )
    }

    override fun onMessageButtonClick(messageAction: MessageAction) {
        when (messageAction) {
            MessageAction.EMAIL -> openMailApp()
            MessageAction.CONTINUE -> finish()
            MessageAction.LOGIN -> login()
            MessageAction.LOGIN_FROM_SIGNUP -> loginFromSignUp()
            MessageAction.ACCOUNT_CREATED -> login()
            MessageAction.PASSWORD_CHANGE -> passwordCreated()
            MessageAction.PROJECT_CREATED -> projectUpdated()
        }
    }

    private fun projectUpdated() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun passwordCreated() {
        finish()
    }

    private fun login() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun loginFromSignUp() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openMailApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        startActivity(intent)
        finish()
    }
}

enum class MessageAction {
    EMAIL, LOGIN, LOGIN_FROM_SIGNUP, CONTINUE, ACCOUNT_CREATED, PASSWORD_CHANGE, PROJECT_CREATED
}
