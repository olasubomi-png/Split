package com.multiflex.assistant

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Color
import android.graphics.Typeface

class MainActivity : Activity() {
    private val navy = Color.rgb(13, 24, 43)
    private val cyan = Color.rgb(0, 180, 210)
    private val ink = Color.rgb(28, 39, 56)
    private val muted = Color.rgb(95, 109, 128)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(246, 249, 252))
            setPadding(dp(20), dp(22), dp(20), dp(18))
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val brand = TextView(this).apply {
            text = "MultiFlex"
            textSize = 30f
            setTypeface(null, Typeface.BOLD)
            setTextColor(navy)
        }
        content.addView(brand)
        content.addView(text("MULTITASKING ASSISTANT", 11f, cyan, true).apply {
            setPadding(0, dp(3), 0, dp(18))
        })
        content.addView(card(
            "Keep apps available",
            "Android or HiOS may restrict background activity to save battery. Use these shortcuts to review the settings for the apps you rely on."
        ))
        content.addView(card(
            "Gaming Mode • eFootball",
            "Use this checklist before playing:\\n1. Turn Battery Saver / Power Saving OFF.\\n2. Open eFootball's App info → Battery and allow background activity or choose Unrestricted if available.\\n3. In Phone Master, check Auto-start and battery-saving controls if present.\\n4. Open Recent Apps and lock eFootball if your HiOS version offers a lock.\\n5. Test by switching to another app for 20–30 seconds, then return.\\n\\nThese settings may improve retention but cannot guarantee the game stays in RAM."
        ))
        content.addView(actionButton("Open eFootball settings guide") {
            AlertDialog.Builder(this)
                .setTitle("eFootball on TECNO POP 9")
                .setMessage("Open Settings → App Management → App List → eFootball → Battery. Choose Unrestricted or Allow background activity if available. Then check Phone Master → Toolbox → Auto-start Management and Battery Lab / Power Marathon. Finally, open Recent Apps and lock eFootball if the lock option exists. Menu names vary by HiOS version.")
                .setPositiveButton("Open Settings") { _, _ ->
                    launchSettings(Intent(Settings.ACTION_SETTINGS))
                }
                .setNegativeButton("Close", null)
                .show()
        })
        content.addView(actionButton("Open battery settings") {
            launchSettings(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        })
        content.addView(actionButton("Open app battery settings") {
            launchSettings(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            })
        })
        content.addView(actionButton("Open Auto-start / background settings") {
            showAutoStartHelp()
        })
        content.addView(card(
            "Split Screen • new shortcut",
            "Use this shortcut to start the split-screen setup for eFootball. MultiFlex cannot activate split-screen by itself; choose Split screen from HiOS Recent apps if that option is available."
        ))
        content.addView(actionButton("Set up Split Screen for eFootball") {
            AlertDialog.Builder(this)
                .setTitle("Start split-screen setup")
                .setMessage("1. Tap Continue to open eFootball.\n2. Open Recent apps (swipe up and hold, or tap the Recent apps button).\n3. Tap eFootball's app icon/menu and choose Split screen if shown.\n4. Choose the second app.\n\nIf Split screen is missing, HiOS or eFootball may not support it on this device/version. This button cannot force the system to enable it.")
                .setPositiveButton("Continue to eFootball") { _, _ ->
                    launchEfootballOrShowHelp()
                }
                .setNegativeButton("Cancel", null)
                .show()
        })
        content.addView(actionButton("Open split-screen instructions") {
            AlertDialog.Builder(this)
                .setTitle("Use split-screen on HiOS")
                .setMessage("Open eFootball first, then open Recent apps. Tap the eFootball icon or its menu and select Split screen if available, then choose another app. If the option is missing, this phone/software or the game may not allow it. Try another compatible app to check whether split-screen is available at all.")
                .setPositiveButton("Open Settings") { _, _ -> launchSettings(Intent(Settings.ACTION_SETTINGS)) }
                .setNegativeButton("Close", null)
                .show()
        })
        content.addView(card(
            "Important",
            "MultiFlex cannot force other apps to stay alive, increase physical RAM, or override Android's memory manager. Avoid aggressive 'RAM cleaner' apps; they can make apps restart more often."
        ))
        content.addView(text("MultiFlex 1.2 • No root • No account • No data collection", 12f, muted, false).apply {
            setPadding(0, dp(16), 0, dp(12))
        })
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun card(title: String, body: String): LinearLayout {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            setBackgroundColor(Color.WHITE)
            elevation = dp(1).toFloat()
        }
        box.addView(text(title, 17f, ink, true))
        box.addView(text(body, 14f, muted, false).apply { setPadding(0, dp(7), 0, 0) })
        val params = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) }
        box.layoutParams = params
        return box
    }

    private fun actionButton(label: String, action: () -> Unit): Button {
        return Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 15f
            setTextColor(Color.WHITE)
            setBackgroundColor(navy)
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) }
        }
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
            gravity = Gravity.START
        }

    private fun launchEfootballOrShowHelp() {
        // Package name used by the standard eFootball Android release.
        val intent = packageManager.getLaunchIntentForPackage("com.konami.pesam")
        if (intent != null) {
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                showEfootballNotFoundHelp()
            } catch (_: SecurityException) {
                showEfootballNotFoundHelp()
            }
        } else {
            showEfootballNotFoundHelp()
        }
    }

    private fun showEfootballNotFoundHelp() {
        AlertDialog.Builder(this)
            .setTitle("Open eFootball manually")
            .setMessage("MultiFlex couldn't open eFootball automatically. Close this message, open eFootball from your home screen, then open Recent apps and choose Split screen from the game's menu if available.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAutoStartHelp() {
        AlertDialog.Builder(this)
            .setTitle("Find Auto-start on TECNO / HiOS")
            .setMessage(
                "Settings names vary by HiOS version. Try Settings → Apps → App management → [target app] → Battery, and look for Allow background activity or Auto-start. Also check Settings → Battery for Power Marathon / power-saving controls. If a menu is missing, your HiOS version may not expose it."
            )
            .setPositiveButton("Open Settings") { _, _ ->
                launchSettings(Intent(Settings.ACTION_SETTINGS))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun launchSettings(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        } catch (_: SecurityException) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
