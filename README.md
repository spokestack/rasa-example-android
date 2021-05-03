# Spokestack Rasa Sample Integration

This is a sample project that demonstrates how to integrate the Android version of [Spokestack Tray](https://github.com/spokestack/spokestack-tray-android) with [Rasa Open Source](https://rasa.com/open-source/).


## What You'll Need

### A Spokestack Account

They're free! Sign up [on our website](https://www.spokestack.io/create).

### A Rasa Bot

```bash
mkdir spokestack-rasa && cd spokestack-rasa
python3 -m venv spokestack-rasa
source spokestack-rasa/bin/activate
pip install rasa
rasa init

# follow the on-screen prompts, using the default options, then...

rasa run
```

### Ngrok

- [Install ngrok](https://ngrok.com/download)

```bash
ngrok http 5005
# copy the https:// address for the next step
```

### This Repo

Make the following substitutions in [`MainActivity`](https://github.com/spokestack/rasa-example-android/blob/main/app/src/main/java/io/spokestack/spokestackrasa/MainActivity.kt):

```kotlin
    override fun getTrayConfig(): TrayConfig {
        // 1: paste the ngrok URL you copied earlier
        val rasaOssURL = "https://<ngrok-tunne>.ngrok.io"
        val wakewordURL = "https://d3dmqd7cy685il.cloudfront.net/model/wake/spokestack"
        return TrayConfig.Builder()
            // 2: swap in an API ID and secret key (found in the "credentials"
            //    section in your Spokestack account)
            .credentials("client-id", "secret-key")
            .wakewordModelURL(wakewordURL)
            .rasaOssURL("$rasaOssURL/webhooks/rest/webhook")
            .logLevel(EventTracer.Level.PERF.value())
            .withListener(this)
            .build()
    }
```
