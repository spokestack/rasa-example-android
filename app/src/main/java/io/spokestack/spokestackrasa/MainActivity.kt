package io.spokestack.spokestackrasa

import android.os.Bundle
import io.spokestack.spokestack.dialogue.DialogueEvent
import io.spokestack.spokestack.nlu.NLUResult
import io.spokestack.spokestack.util.EventTracer
import io.spokestack.tray.*

class MainActivity : TrayActivity(), SpokestackTrayListener {
    private val greeting = "Welcome! This example uses Minecraft sample models. " +
            "Try saying, \"How do I make a castle?\""
    private var lastResponse: String = greeting

    override fun getTrayConfig(): TrayConfig {
        // change to match your local ngrok URL
        val rasaOssURL = "https://<ngrok-tunnel>.ngrok.io"
        val wakewordURL = "https://d3dmqd7cy685il.cloudfront.net/model/wake/spokestack"
        return TrayConfig.Builder()
            // use an ID and secret key from the "credentials" section in your Spokestack account
            .credentials( "client-id", "secret-key")
            .wakewordModelURL(wakewordURL)
            .rasaOssURL("$rasaOssURL/webhooks/rest/webhook")
            .logLevel(EventTracer.Level.INFO.value())
            .withListener(this)
            .build()
    }

    override fun getTrayListener(): SpokestackTrayListener {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onLog(message: String) {
        println("LOG: $message")
    }

    override fun onDialogueEvent(event: DialogueEvent): Prompt? {
        val prompt = tray.spokestack().finalizePrompt(event.state.prompt)
        val end = prompt.text.contains(Regex("bye", RegexOption.IGNORE_CASE))

        return when (event.type) {
            DialogueEvent.Type.ACTION ->
                Prompt("", imageURL = prompt.text, expectFollowup = !end)
            DialogueEvent.Type.PROMPT ->
                Prompt(prompt.text, expectFollowup = !end)
            else -> null
        }
    }

    override fun onError(error: Throwable) {
        println("ERROR: ${error.localizedMessage}")
    }

    override fun onOpen() {
        println("OPENED")
    }

    override fun onClose() {
        println("CLOSED")
    }
}
