# PALASH AI 🌱

### AI-Powered Mother-Tongue Learning for Primary Education

PALASH AI is an offline-first Android application designed to support **mother-tongue-based multilingual education (MTB-MLE)** in resource-constrained classrooms.

The application helps Hindi-medium teachers deliver learning support in **Santali**, enabling students to understand concepts through their mother tongue while reducing the need for teachers to have additional language training.

The solution is designed around the requirements of the **PALASH MTB-MLE initiative in Jharkhand**, with a focus on low-end Android devices, limited connectivity, offline access, and scalable Indian-language AI.

---

## 🎯 Problem

In multilingual classrooms, students may not have sufficient proficiency in the language used for classroom instruction.

This creates a communication barrier between teachers and students, affecting:

- Conceptual understanding
- Classroom participation
- Foundational learning
- Teacher-student communication
- Access to learning resources

The problem becomes particularly significant in tribal and multilingual communities where the student's mother tongue may differ substantially from the language used by the teacher.

PALASH AI addresses this gap by providing teachers with a digital layer for **mother-tongue-supported teaching and learning**.

---

## 💡 Our Solution

PALASH AI provides a dedicated Android application that enables teachers to:

- Access curriculum-aligned learning content
- Translate Hindi classroom input into Santali
- Provide translated text and voice output
- Use lessons, flashcards and worksheets
- Access learning content without continuous internet connectivity
- Support students using their mother tongue during classroom instruction

The system is designed to work on **resource-constrained Android devices** and follows an **offline-first architecture**.

---

# 🚀 Key Features

### 🗣️ Hindi → Santali Translation

Teachers can provide Hindi input and receive Santali translation through the application's language pipeline.

The current implementation integrates **IndicTrans2-based translation** for Hindi-to-Santali language processing.

---

### 🎙️ Speech Input

Teachers can provide classroom input using speech.

The application uses Android's `AudioRecord` API to capture microphone audio for speech processing.

---

### 🔊 Voice Output

Translated content can be presented as voice output using Indian-language text-to-speech capabilities.

This allows students to receive learning support through both:

- Text
- Audio

---

### 📚 Curriculum-Integrated Learning

PALASH AI is not limited to translation.

The application provides structured educational content including:

- Lessons
- Learning objectives
- Activities
- Assessments
- Flashcards
- Worksheets

This allows language assistance to be integrated directly into classroom learning.

---

### 📱 Offline-First Architecture

The application is designed for environments where reliable internet connectivity cannot be assumed.

Important learning content is stored locally so that core educational functionality remains available without continuous network access.

---

### 💾 Local Data Storage

PALASH AI uses **Room Database** for structured local storage of educational content and application data.

This enables:

- Local curriculum storage
- Lesson retrieval
- Offline access
- Structured educational data management

---

### ⚡ Designed for Low-End Devices

The solution is designed with resource-constrained classrooms in mind.

The architecture minimizes dependence on:

- Cloud APIs
- Continuous internet connectivity
- Heavy backend infrastructure
- External authentication services

This makes the solution suitable for deployment in low-connectivity educational environments.

---

# 🏗️ Technical Architecture

PALASH AI follows a modular Android architecture.

```text
                    PALASH AI
                        │
                        ▼
              Android Application
              Kotlin + Jetpack Compose
                        │
                        ▼
                MVVM Architecture
                        │
             ┌──────────┴──────────┐
             │                     │
             ▼                     ▼
        User Input            Learning Content
             │                     │
       Text / Speech          Lessons / Flashcards
             │                  / Worksheets
             ▼                     │
       AudioRecord                │
             │                     │
             ▼                     │
           ASR                     │
      Speech → Text                │
             │                     │
             ▼                     │
        Translation                │
       Hindi → Santali             │
             │                     │
             ▼                     │
            TTS                    │
       Text → Speech               │
             │                     │
             └──────────┬──────────┘
                        ▼
                Mother-Tongue
                   Learning
                        │
                        ▼
                 Room Database
                        │
                        ▼
                 Offline Access
