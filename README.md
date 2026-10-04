# Money Tracker 💰

A simple, ultra-fast, mobile-first Android personal spending notebook designed to help you quickly record expenses, track your monthly spending at a glance, write spending reminders, and view this month's total right on your home screen.

---

## 🌟 Key Features

- **⚡ Lightning-Fast Spend Logging**:
  - Add expenses in seconds with large touch targets.
  - Inputs: Amount (`₹` INR), Description ("What did you spend on?"), optional Category chips (*Food*, *Travel*, *Shopping*, *Other*), and Date (defaults to Today).

- **📊 Monthly Spending at a Glance**:
  - Prominent **This Month** summary card on the Home screen.
  - Displays formatted totals (e.g., `₹4,250`).
  - View recent transactions ordered newest-first.

- **📑 Full Transaction History**:
  - Review all past transactions with clear date and category labels.
  - Automatic calculation of **This Month's Total** and **All-Time Total**.
  - Category filters (*All*, *Food*, *Travel*, *Shopping*, *Other*).
  - Edit existing transactions or delete them with an explicit confirmation dialog.

- **📝 Personal Notes & Reminders**:
  - Clean, dedicated notebook section for personal spending notes.
  - Perfect for keeping tabs like:
    - *"Paid ₹500 to Ravi for dinner"*
    - *"Need to remember the electricity payment"*
    - *"Bought headphones on October 2"*
  - Timestamped with creation date and time.
  - Easily add, edit, or delete notes.

- **📱 Android Home-Screen Widget**:
  - Minimalist, distraction-free widget displaying **ONLY** the current month's total spending:
    ```
    ┌──────────────────────┐
    │   Money Tracker      │
    │                      │
    │      ₹4,250          │
    │   Spent This Month   │
    └──────────────────────┘
    ```
  - Automatically updates immediately whenever an expense is added, edited, or deleted.
  - Accurately rolls over and displays the new total at the start of each month.
  - Tap anywhere on the widget to open the Money Tracker app.

---

## 🛡️ Privacy & Simplicity First

- **🚫 No Ads**: Completely clean and distraction-free interface.
- **🔒 100% Offline & Private**: All data is stored strictly on your local device.
- **🙅 No Accounts or Logins**: No cloud sync, no tracking, and no internet access required.
- **✨ No Over-Engineering**: No bank account linking, complex budgeting calculators, loans, or push notifications. Acts like your trusted personal digital pocketbook.

---

## 🛠️ Technology Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 (M3)
- **Local Database**: [Android Room Database](https://developer.android.com/training/data-storage/room) with SQLite
- **Annotation Processing**: [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)
- **Asynchronous & Reactive**: Kotlin Coroutines & StateFlow
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Android Widget**: `AppWidgetProvider` + `RemoteViews`
- **Testing**: Robolectric & JUnit4 for JVM-based unit testing

---

## 📲 How to Add the Home-Screen Widget

1. Long-press on any empty space on your Android home screen.
2. Tap **Widgets**.
3. Scroll down and find **Money Tracker**.
4. Touch and hold the **Money Tracker (Spent This Month)** widget, then drag it to your desired spot on the home screen.
5. Resize the widget if desired. It will always keep your current month's total spending up to date!

---

## 🚀 Installation & Build Process

### Prerequisites
- Android Studio Ladybug / Meerkat or later (or command-line Gradle)
- JDK 17 or 21
- Android SDK with API 36 / 35 (minimum SDK is 24)

### Building the APK
To build the debug APK:
```bash
gradle assembleDebug
```
The generated APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Installing on Device / Emulator
Connect an Android device or start an Android emulator, then run:
```bash
gradle installDebug
```

### Running Unit Tests
To execute local JVM Robolectric unit tests:
```bash
gradle :app:testDebugUnitTest
```

---

## 🆕 What's New in Version 1.0.0

- **Initial Release** of the fast, mobile-first Money Tracker.
- Modern Material 3 user interface with dynamic dark mode and emerald branding.
- Quick Add Spend dialog with single-tap category chips and date selection.
- Home screen with "This Month" spending highlight and recent spend list.
- Comprehensive Transactions screen with monthly vs all-time totals and category filtering.
- Spending-focused Notes section with timestamps.
- Native Android Home-Screen Widget updating synchronously on spend changes.
- Safe local persistence using Android Room.
