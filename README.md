# AppSolarMovil 📱

Aplicación Android nativa para monitoreo de energía solar en tiempo real, desarrollada en **Kotlin + Jetpack Compose**. Se conecta al backend **Olympus API** para mostrar métricas solares, pronósticos y recomendaciones generadas por IA.

---

## Características

- **Dashboard solar** — radiación, temperatura, UV, índice solar, amanecer/atardecer y horas óptimas en tiempo real
- **Pronóstico 16 días** — proyección de radiación, temperatura y índice solar por día
- **Recomendaciones IA** — análisis energético personalizado según el perfil de la empresa
- **Chat IA** — asistente conversacional energético integrado con el modelo LLM del backend
- **Indicador de estado solar** — semáforo visual (Bajo / Medio / Alto / Excelente)
- **Notificaciones internas** — alertas generadas a partir de las respuestas de la IA

---

## Stack tecnológico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM |
| Navegación | Bottom Navigation (2 tabs) |
| Red | Retrofit 2 + OkHttp (timeout 40 s) |
| Estado | StateFlow + Coroutines |
| Almacenamiento local | DataStore Preferences |
| Imágenes | Coil |
| Build | Gradle (KTS) |

---

## Arquitectura

```
AppSolarMovil/
└── AppSolar/
    └── app/src/main/java/com/example/appsolar/
        ├── MainActivity.kt          # Entry point
        ├── Model/
        │   ├── ApiService.kt        # Definición de endpoints Retrofit
        │   ├── RetrofitClient.kt    # Singleton de Retrofit
        │   ├── Solar_Today.kt       # Modelos SolarToday, SolarData, SolarScore
        │   ├── ForecastData.kt      # Modelos de pronóstico
        │   ├── ChatRequest.kt       # DTOs del chat IA
        │   ├── RecomendationRequest.kt # DTOs de recomendaciones
        │   ├── SolarStatus.kt       # Lógica de estado visual
        │   ├── BottomItem.kt        # Items de navegación inferior
        │   ├── NotificationsItem.kt # Modelo de notificaciones
        │   └── ParseDate.kt         # Utilidades de fecha
        ├── View/
        │   ├── ScaffoldScreen.kt    # Navegación principal (Scaffold + BottomBar)
        │   ├── DashBoardScreen.kt   # Pantalla principal con datos solares
        │   ├── ChaIAScreen.kt       # Pantalla de chat con IA
        │   └── BottomBar.kt         # Barra de navegación inferior
        ├── ViewModel/
        │   ├── SolarViewModel.kt            # Datos solares del día + score
        │   ├── ForecastViewModel.kt         # Pronóstico solar
        │   ├── ViewModelRecomendations.kt   # Recomendaciones IA + fallback local
        │   └── ChatIAViewModel.kt           # Estado del chat IA
        └── ui/theme/               # Colores, tipografía y tema Material 3
```

---

## Pantallas

### Dashboard
Muestra en tiempo real:
- Índice solar (0–100) con indicador visual coloreado
- Radiación solar (kWh/m²), temperatura (°C), viento (km/h), UV
- Amanecer y atardecer
- Horas óptimas de generación solar
- Pronóstico de los próximos 16 días
- Recomendaciones energéticas generadas por IA

### Chat IA
- Conversación en lenguaje natural con el asistente energético
- Envía el perfil de la empresa (tipo, consumo, cargas principales) junto con cada mensaje
- Animación de escritura en respuestas del bot

---

## Endpoints consumidos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/solar/today` | Datos solares del día actual |
| `GET` | `/api/solar/score` | Resumen textual del score energético |
| `GET` | `/api/solar/forecast?days=16` | Pronóstico solar de hasta 16 días |
| `POST` | `/api/ai/recommendations` | Recomendaciones energéticas por perfil |
| `POST` | `/api/ai/chat` | Chat conversacional con el asistente IA |

---

## Configuración

### Requisitos
- Android Studio Hedgehog o superior
- Android SDK 24+ (Android 7.0 mínimo)
- Kotlin 2.x
- Gradle 8.x

### Cambiar la URL del backend

Edita `AppSolar/app/src/main/java/com/example/appsolar/Model/RetrofitClient.kt`:

```kotlin
.baseUrl("http://<IP_DEL_SERVIDOR>:5016/api/")
```

Reemplaza `<IP_DEL_SERVIDOR>` con la IP donde corre el backend Olympus.

> ⚠️ El manifesto tiene `android:usesCleartextTraffic="true"` habilitado para permitir HTTP en desarrollo. En producción usa HTTPS y elimina esta configuración.

### Compilar y ejecutar

1. Abre `AppSolar/` en Android Studio
2. Conecta un dispositivo físico o inicia un emulador (API 24+)
3. Actualiza la `baseUrl` con la IP del servidor Olympus
4. Ejecuta `Run > Run 'app'`

---

## Indicador solar

| Rango | Estado | Color |
|-------|--------|-------|
| 0 – 30 | Bajo | 🔴 Rojo |
| 31 – 60 | Medio | 🟡 Amarillo |
| 61 – 80 | Alto | 🟢 Verde |
| 81 – 100 | Excelente | 💚 Verde brillante |

---

## Parte del ecosistema Olympus

Este repositorio es un componente del monorepo [Olympus](https://github.com/JuanPabloMendozaLopez/Olympus):

```
Olympus/
├── Olympus/          ← Backend ASP.NET Core (API REST)
├── solar-ai/         ← Frontend web (Laravel + Blade)
└── app-movil/        ← Esta app Android
```
