# Money Tracker (RupeeLog)

A simple, ultra-fast, mobile-first Android personal spending notebook designed to quickly record expenses, track monthly spending, write spending reminders, and view this month's total from the Android home screen.

## Technology Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Design System:** Material 3
- **Local Database:** Room + SQLite
- **Annotation Processing:** KSP
- **Async & Reactive:** Kotlin Coroutines + StateFlow
- **Architecture:** MVVM + Repository Pattern
- **Android Widget:** AppWidgetProvider + RemoteViews
- **Testing:** JUnit4 + Robolectric
- **Minimum SDK:** Android 7.0 (API 24)

The application is completely local and does not require a backend, cloud database, or internet connection.

## Installation & Build

### Prerequisites

- Android Studio Ladybug, Meerkat, or newer
- JDK 17 or newer
- Android SDK
- Android device or emulator
- USB debugging enabled when using a physical device

### Clone the Repository

```bash
git clone https://github.com/aniilhr/money-tracker-android.git
cd money-tracker-android
```

### Build the Debug APK

Linux/macOS:

```bash
./gradlew assembleDebug
```

Windows:

```powershell
.\gradlew.bat assembleDebug
```

Generated APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Install on a Connected Android Device

Linux/macOS:

```bash
./gradlew installDebug
```

Windows:

```powershell
.\gradlew.bat installDebug
```

Or install the generated APK manually:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Run Tests

```bash
./gradlew :app:testDebugUnitTest
```

Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

## Features

### Fast Spend Logging

- Add expenses in seconds.
- Amount in INR.
- Description.
- Optional category: Food, Travel, Shopping, Other.
- Date defaults to Today.

### Monthly Spending at a Glance

- Prominent **This Month** spending total.
- Formatted INR totals such as `₹4,250`.
- Recent transactions ordered newest first.
- Automatic monthly total calculation.
- All-time spending total.

### Transaction History

- View all recorded transactions.
- Amount, description, category, and date.
- Category filters:
  - All
  - Food
  - Travel
  - Shopping
  - Other
- Edit transactions.
- Delete transactions with confirmation.

### Personal Notes

A simple notebook for spending-related reminders.

Examples:

- "Paid ₹500 to Ravi for dinner"
- "Need to remember the electricity payment"
- "Bought headphones on October 2"

Each note stores:

- Note text
- Created date and time
- Updated date and time

Notes can be added, edited, and deleted.

### Android Home Screen Widget

A native Android widget showing only the current month's spending.

```text
┌──────────────────────┐
│   Money Tracker      │
│                      │
│      ₹4,250          │
│   Spent This Month   │
└──────────────────────┘
```

The widget:

- Shows only the current month's total.
- Does not show individual transactions.
- Does not show categories.
- Does not show notes.
- Updates when an expense is added, edited, or deleted.
- Automatically reflects the new month's total.
- Opens the app when tapped.

## Privacy

Money Tracker is designed to be completely local.

- 100% offline.
- All data stays on the device.
- No account or login.
- No cloud database.
- No Firebase.
- No analytics.
- No advertising.
- No tracking.
- No external APIs.
- No internet connection required.

## Design Principles

The application intentionally avoids unnecessary complexity.

- Clean and minimal interface.
- No decorative gradients.
- No excessive pill-shaped controls.
- No unnecessary animations.
- No excessive scroll effects.
- No fake statistics or metrics.
- No fake reviews or testimonials.
- No AI-generated photographs.
- No unnecessary onboarding.
- No promotional screens.
- Prioritize speed, readability, accessibility, and one-hand use.
- Prefer native Android components where practical.

The app should feel like a practical personal utility rather than a marketing product.

## Core Workflow

```text
Add Spend
    ↓
Save Locally
    ↓
Calculate Monthly Total
    ↓
View Transactions
    ↓
Add Notes
    ↓
Update Widget
```

## Widget Setup

1. Long-press an empty area on the Android home screen.
2. Tap **Widgets**.
3. Find **Money Tracker**.
4. Touch and hold the widget.
5. Drag it onto the home screen.
6. Resize it if required.

The widget automatically displays the current month's spending total.

## Scope

Money Tracker deliberately does not include:

- Income tracking
- Bank accounts
- Bank synchronization
- Budget planning
- Investments
- Loans
- Credit cards
- Complex charts
- Financial reports
- Social features
- Notifications
- Cloud synchronization
- Advertising

## Version 1.0.0

Initial release including:

- Fast expense entry.
- INR currency support.
- Monthly spending summary.
- All-time spending total.
- Transaction history.
- Category filtering.
- Expense editing and deletion.
- Delete confirmation.
- Personal notes.
- Native Android home-screen widget.
- Local Room database.
- Dark mode support.
- Offline-first operation.
- Minimal Material 3 interface.
