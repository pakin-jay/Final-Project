# CourtSmash — Badminton Court Booking Application

**CourtSmash** is a modern, premium badminton court booking application built natively for Android using Kotlin, Jetpack Compose, and Material 3. Designed around the elegant visual identity of **"Holy Light"** (warm ivory white, champagne gold accents, soft ambient glow, generous whitespace, and luxury sports-club aesthetics).

---

## Requirement Mapping Checklist

| Requirement | Implementation Summary | Status |
| :--- | :--- | :---: |
| **1. Multiple Screens & Navigation** | Integrated **8 distinct screens** using Navigation Compose with type-safe arguments and backstack management: Home/Explore, Court Detail, Booking Form, Booking Success, My Bookings, Booking Detail, AI Court Matcher, Profile/Settings. | **COMPLETE** |
| **2. Data Persistence** | Full **Room Database** persistence for Courts and Bookings entities with transactional overlap validation, plus **DataStore Preferences** for user settings. | **COMPLETE** |
| **3. External API Integration** | Real-time weather data fetched from **Open-Meteo REST API** using **Retrofit & Kotlin Coroutines** with non-blocking offline resilience. | **COMPLETE** |
| **4. Real AI Functionality** | AI Court Assistant integrating Gemini-compatible HTTPS backend contract. Independent in-app price calculation and local rule-based fallback engine when backend is unconfigured. | **COMPLETE** |

---

## 🏛️ Application Architecture

The application adopts a clean, maintainable **MVVM (Model-View-ViewModel)** architecture:

```text
com.example.courtsmash/
├── MainActivity.kt               # Single Activity host
├── CourtSmashApp.kt              # App Scaffold with Bottom Navigation Bar
├── navigation/                   # Screen routes, NavItem, NavGraph, AppViewModelFactory
├── domain/
│   └── model/                    # Court, Booking, WeatherInfo, AiRecommendation models
├── data/
│   ├── local/                    # Room Database (CourtEntity, BookingEntity, DAOs, PreferencesManager)
│   ├── remote/                   # Retrofit services (Open-Meteo Weather API, AI Backend DTOs)
│   └── repository/               # CourtRepository, BookingRepository, WeatherRepository, AiRepository, UserRepository
├── ui/
│   ├── theme/                    # Holy Light Theme (Color, Type, Shape, Theme)
│   ├── components/               # Reusable Holy Light cards, buttons, chips, badges, weather widgets
│   ├── home/                     # Home/Explore Screen & HomeViewModel
│   ├── court/                    # Court Detail Screen & CourtDetailViewModel
│   ├── booking/                  # Booking Form Screen, Success Screen & BookingViewModel
│   ├── bookings/                 # My Bookings Screen, Booking Detail Screen & MyBookingsViewModel
│   ├── ai/                       # AI Assistant Screen & AiAssistantViewModel
│   └── profile/                  # Profile & Settings Screen & ProfileViewModel
```

---

## 🎨 Visual Identity — "Holy Light"

- **Primary Background:** Warm Ivory White (`#FAF9F6`)
- **Surface:** Pure White (`#FFFFFF`)
- **Primary Accent:** Champagne Gold (`#D4AF70`)
- **Secondary Accent:** Soft Luminous Gold (`#F1DEB5`)
- **Primary Text:** Deep Charcoal (`#242424`)
- **Secondary Text:** Warm Gray (`#85817A`)
- **Cards & Corners:** Soft rounded corners (18–24 dp radii) with subtle golden borders and ambient shadows.

---

## ⚡ Key Business & Booking Logic

1. **Date & Time Validation:** Rejects past booking dates and ensures selected slots fall within operating hours (e.g. 08:00 - 22:00).
2. **Local Overlap Detection:** Uses interval overlap rule: `newStart < existingEnd AND existingStart < newEnd`. Adjacent slots (e.g. 14:00-16:00 and 16:00-18:00) are explicitly permitted.
3. **Monetary Precision:** Safe total price calculation (`court.pricePerHour * durationHours`).
4. **AI Output Validation:** All court recommendations are validated against local candidate court IDs and prices are recalculated in application code to prevent AI arithmetic errors or hallucinations.

---

## ⚙️ Setup & API Configuration

### Prerequisites
- Android Studio Ladybug / Koala / Hedgehog
- JDK 17 / Kotlin 2.2.10
- Minimum SDK: Android 8.0 (API Level 24)
- Target / Compile SDK: Android API Level 36

### API Endpoints
1. **Weather Service:** `https://api.open-meteo.com/v1/forecast` (Pre-configured, no API key required).
2. **AI Service:** `NetworkClient.aiBackendBaseUrl` points to a configurable HTTPS backend endpoint. If unconfigured or offline, the app seamlessly switches to the built-in local AI recommendation engine.

---

## 🧪 Unit Tests

Unit tests are included in `app/src/test/java/com/example/courtsmash/BookingLogicTest.kt`:
- `testPastDateValidation_returnsError()`
- `testOutsideOperatingHours_returnsError()`
- `testOverlappingBooking_returnsError()`
- `testAdjacentBooking_returnsSuccess()`
- `testCreateBooking_calculatesPriceCorrectly()`

To execute tests via Gradle:
```bash
./gradlew testDebugUnitTest
```

---

## 🎬 Demonstration Script for Presentation

1. **Explore Screen:** Launch CourtSmash, point out the "Holy Light" UI theme, search bar, Bangkok live weather banner, facility filter chips, and featured courts.
2. **Court Details:** Tap "Apex Royale Badminton Club", view operating hours, description, facilities, and court-specific weather forecast.
3. **Court Booking:** Tap "Book This Court", pick a date and start time slot (e.g. 14:00, 2 Hours). Show dynamic price breakdown (฿280 × 2 = ฿560).
4. **Overlap Rejection:** Show that attempting to re-book the same 14:00-16:00 slot displays an instant error banner preventing duplicate bookings. Select an adjacent slot (16:00-18:00) to confirm booking.
5. **Booking Success & Persistence:** View confirmation ticket with booking reference (e.g. `CS-89214`). Navigate to "My Bookings" tab to confirm Room DB persistence.
6. **AI Assistant:** Switch to "AI Assist" tab, adjust max budget slider to ฿300, select duration 2h, tap "Generate AI Match", and observe ranked recommendations with match scores and reasoning.
7. **Profile Settings:** Open Profile screen, edit display name, inspect external service status indicators, and demonstrate local history clearing with safety dialog.
