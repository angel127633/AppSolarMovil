package com.example.appsolar.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsolar.Model.ChatRequest
import com.example.appsolar.Model.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatIAViewModel : ViewModel() {

    data class Message(
        val text: String,
        val isUser: Boolean,
        var animated: Boolean = false
    )

    val messages = mutableStateListOf<Message>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {

        messages.add(
            Message(
                "Hola 👋 Soy tu asistente energético.",
                false
            )
        )
    }

    fun sendMessage(text: String) {

        if (text.isBlank()) return

        // MENSAJE USUARIO
        messages.add(
            Message(
                text = text,
                isUser = true,
                animated = true
            )
        )

        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val response =
                    RetrofitClient.api.sendMessage(
                        ChatRequest(
                            message = text,
                            targetType = "company",
                            name = "Hotel Majayura",
                            companyType = "hotel",
                            monthlyConsumptionKwh = 12000,
                            companySize = 18,
                            mainLoads = listOf(
                                "aire acondicionado (30%)",
                                "lavandería industrial (8%)"
                            ),
                            tariffCopKwh = 1050,
                            operatingHoursPerDay = 24
                        )
                    )

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {

                        val reply =
                            response.body()
                                ?.data
                                ?.reply

                        messages.add(
                            Message(
                                reply ?: "Sin respuesta",
                                isUser = false,
                                animated = false
                            )
                        )

                    } else {
                        messages.add(
                            Message(
                                generateFallbackResponse(text),
                                isUser = false,
                                animated = false
                            )
                        )
                    }

                    _isLoading.value = false
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    val fallbackReply = generateFallbackResponse(text)

                    messages.add(
                        Message(
                            fallbackReply,
                            isUser = false,
                            animated = false
                        )
                    )

                    _isLoading.value = false
                }
            }
        }
    }

    private fun generateFallbackResponse(
        question: String
    ): String {

        val lower = question.lowercase()

        return when {

            "aire" in lower ||
                    "a/c" in lower ||
                    "aire acondicionado" in lower -> {

                listOf(

                    "El aire acondicionado suele representar uno de los consumos eléctricos más altos dentro de hoteles, restaurantes y negocios con operación continua ⚡. Una estrategia bastante eficiente es aprovechar las horas de mayor radiación solar entre las 10:00 y las 15:00 ☀️ para enfriar espacios antes del pico nocturno. Esto permite que los equipos trabajen con menor esfuerzo durante la tarde y ayuda a disminuir considerablemente el consumo energético acumulado.\n\nTambién es recomendable apagar equipos en habitaciones vacías, reducir el uso en áreas con poca ocupación y mantener filtros limpios para evitar sobrecarga en los sistemas de climatización. Incluso pequeños ajustes de 1°C o 2°C en el termostato pueden generar ahorros importantes sin afectar demasiado la comodidad de los huéspedes.",

                    "Con las condiciones solares actuales 🌤️, el mejor momento para utilizar el aire acondicionado es durante el bloque de máxima generación solar. En operaciones hoteleras esto puede ayudar a reducir bastante el impacto energético diario, especialmente si se realiza un pre-enfriamiento de las áreas más concurridas antes de las horas de mayor demanda.\n\nAdemás, evitar abrir constantemente puertas y ventanas ayuda a mantener estable la temperatura interna y reduce el tiempo de funcionamiento de los compresores. Una buena gestión del aire acondicionado no solo reduce costos, sino que también prolonga la vida útil de los equipos.",

                    "El consumo del A/C puede aumentar considerablemente durante días calurosos 🔥, especialmente cuando la temperatura exterior supera los 34°C. Por eso es importante aprovechar el periodo de mayor radiación solar para operar los sistemas de climatización de forma más eficiente.\n\nUna recomendación útil es dividir el enfriamiento por zonas, apagar equipos innecesarios y utilizar temperaturas moderadas en lugar de enfriamiento extremo. Estas pequeñas acciones pueden reducir bastante la carga energética diaria y mejorar el rendimiento operativo del negocio."
                ).random()
            }

            // =========================================
            // MEJORAR SISTEMA SOLAR
            // =========================================

            ("mejorar" in lower ||
                    "optimizar" in lower ||
                    "rendimiento" in lower) &&

                    ("panel" in lower ||
                            "solar" in lower ||
                            "paneles" in lower) -> {

                listOf(

                    "Para mejorar el rendimiento de tu sistema solar ☀️ es importante revisar primero los horarios donde ocurre el mayor consumo energético. En negocios como hoteles o restaurantes, mover cargas pesadas como aire acondicionado, lavandería y refrigeración hacia las horas de mayor radiación solar puede aumentar muchísimo el aprovechamiento energético.\n\nTambién es recomendable realizar limpieza frecuente de los paneles, ya que el polvo y la suciedad pueden reducir la eficiencia del sistema. Otra mejora importante es monitorear el consumo en tiempo real para detectar equipos que estén consumiendo más energía de lo normal.",

                    "Una forma eficiente de optimizar un sistema de paneles solares ⚡ es reducir el consumo innecesario durante horas nocturnas y concentrar las cargas pesadas entre las 10:00 y 15:00 ☀️. Esto permite utilizar más energía generada directamente por los paneles y depender menos de la red eléctrica.\n\nAdemás, revisar inversores, sombras sobre los paneles y mantenimiento preventivo puede ayudar a mejorar notablemente el rendimiento energético del sistema.",

                    "Muchos sistemas solares pierden eficiencia no por falta de paneles, sino por una mala distribución del consumo energético 🌤️. Una buena estrategia consiste en programar equipos de alto consumo durante el mediodía solar y evitar desperdicios energéticos en horarios nocturnos.\n\nTambién ayuda bastante revisar periódicamente la limpieza de los paneles, posibles sombras y el estado del inversor para garantizar que el sistema opere al máximo rendimiento."
                ).random()
            }

            // =========================================
            // INSTALAR PANELES
            // =========================================

            ("instalar" in lower ||
                    "poner" in lower ||
                    "comprar" in lower) &&

                    ("panel" in lower ||
                            "solar" in lower ||
                            "paneles" in lower) -> {

                listOf(

                    "Antes de instalar paneles solares ☀️ es importante analizar el consumo energético diario de la empresa para calcular correctamente el tamaño del sistema. Negocios con alto consumo diurno suelen obtener mejores resultados porque aprovechan directamente la energía generada durante el día.\n\nTambién se recomienda evaluar el espacio disponible, orientación del techo y posibles sombras que puedan afectar la producción solar.",

                    "La instalación de paneles solares ⚡ puede ayudarte a reducir considerablemente el gasto energético mensual, especialmente en ciudades con alta radiación solar como Riohacha ☀️. Para obtener mejores resultados, es importante diseñar el sistema según el consumo real del negocio y priorizar equipos de alto consumo durante horas solares.",

                    "Un sistema solar bien dimensionado 🌤️ puede generar ahorros importantes a largo plazo. Antes de instalar paneles es recomendable revisar los equipos que más consumen energía y definir cuáles cargas se usarán principalmente durante el día."
                ).random()
            }

            "panel" in lower ||
                    "solar" in lower ||
                    "paneles" in lower -> {

                listOf(

                    "Riohacha posee uno de los niveles de radiación solar más altos de Colombia ☀️, por lo que implementar paneles solares puede convertirse en una excelente estrategia para disminuir costos energéticos y estabilizar el gasto operativo mensual. Negocios como hoteles, restaurantes y comercios con consumo diurno constante suelen beneficiarse mucho porque aprovechan directamente la energía generada durante las horas de mayor producción.\n\nAdemás del ahorro económico, un sistema solar bien diseñado ayuda a reducir dependencia de la red eléctrica y ofrece mayor estabilidad frente a variaciones tarifarias o cortes de energía. Mientras más consumo tengas durante el día, mayor provecho podrás obtener de la generación fotovoltaica.",

                    "Los paneles solares funcionan especialmente bien en operaciones que mantienen equipos activos durante varias horas al día ⚡. Por ejemplo, sistemas de aire acondicionado, iluminación, lavandería y refrigeración pueden consumir gran parte de la energía generada directamente desde el sistema solar.\n\nOtra ventaja importante es que desplazar cargas pesadas al mediodía permite aprovechar el momento de máxima producción solar, reduciendo todavía más el costo energético diario. Esto mejora la eficiencia general y ayuda a recuperar la inversión más rápidamente.",

                    "Implementar energía solar no significa únicamente instalar paneles ☀️. También implica optimizar horarios de consumo, distribuir correctamente las cargas eléctricas y reducir desperdicios energéticos. Cuando estas estrategias trabajan juntas, los resultados suelen ser mucho más notorios.\n\nEn negocios con consumo elevado, una combinación de paneles solares y gestión inteligente del consumo puede representar una reducción importante en la factura eléctrica mensual y mejorar considerablemente la sostenibilidad operativa."
                ).random()
            }

            "lavanderia" in lower ||
                    "lavandería" in lower ||
                    "lavado" in lower -> {

                listOf(

                    "La lavandería industrial suele convertirse en una de las cargas más exigentes dentro de operaciones hoteleras ⚡, especialmente por el uso simultáneo de motores, calentadores, secadoras y sistemas de bombeo. Una forma eficiente de reducir costos es mover la mayor cantidad posible de ciclos de lavado hacia las horas de máxima radiación solar entre las 10:00 y 15:00 ☀️.\n\nEsto permite aprovechar mejor la energía disponible durante el día y disminuye la presión energética durante la noche, donde normalmente el consumo resulta más costoso. También es recomendable evitar operar varios equipos de alto consumo al mismo tiempo para reducir picos de demanda.",

                    "Las áreas de lavandería consumen bastante energía debido a procesos continuos de lavado y secado 🌤️. Una buena práctica energética consiste en programar los ciclos más pesados durante el mediodía solar y dejar procesos ligeros para horas de menor generación.\n\nTambién ayuda bastante realizar mantenimiento frecuente en secadoras y motores, ya que equipos en mal estado suelen trabajar con mayor esfuerzo y desperdiciar energía innecesariamente. Optimizar estos procesos puede representar un ahorro importante a largo plazo.",

                    "En hoteles y negocios similares, la lavandería puede representar una parte considerable del consumo eléctrico diario ⚡. Aprovechar las horas de mayor radiación solar para ejecutar ciclos completos de lavado ayuda a mejorar la eficiencia energética y reducir el impacto económico del consumo.\n\nOtra estrategia útil consiste en agrupar cargas completas en lugar de hacer ciclos pequeños repetitivos. Esto reduce tiempo de operación, disminuye desgaste de equipos y mejora el rendimiento energético general."
                ).random()
            }

            // =========================================
            // FACTURA ALTA
            // =========================================

            ("factura" in lower ||
                    "recibo" in lower ||
                    "energia" in lower) &&

                    ("alta" in lower ||
                            "cara" in lower ||
                            "mucho" in lower ||
                            "subio" in lower) -> {

                listOf(

                    "Si tu factura eléctrica está aumentando ⚡, probablemente existan cargas de alto consumo funcionando fuera de horarios eficientes. Equipos como aire acondicionado, refrigeración y lavandería suelen representar gran parte del gasto energético mensual.\n\nUna estrategia bastante útil es mover esos consumos hacia las horas de mayor radiación solar ☀️ y reducir el uso innecesario durante la noche.",

                    "Las facturas elevadas normalmente aparecen cuando hay consumos intensivos durante horarios sin generación solar 🌤️. Revisar los equipos que permanecen activos constantemente y reorganizar horarios de operación puede ayudarte a disminuir considerablemente los costos energéticos.",

                    "Una factura energética alta suele indicar que existen equipos consumiendo más energía de lo esperado ⚡. Identificar las cargas principales y aprovechar las horas solares para operar equipos pesados puede generar mejoras importantes en el gasto mensual."
                ).random()
            }

            "ahorro" in lower ||
                    "ahorrar" in lower ||
                    "factura" in lower ||
                    "consumo" in lower -> {

                listOf(

                    "Existen varias estrategias que pueden ayudarte a reducir significativamente el consumo energético ⚡ sin afectar demasiado la operación diaria del negocio. Una de las más efectivas es desplazar cargas pesadas hacia las horas de mayor radiación solar, donde el aprovechamiento energético es mucho más eficiente.\n\nTambién es recomendable monitorear equipos de alto consumo como aire acondicionado, refrigeración y lavandería, ya que normalmente representan gran parte del gasto eléctrico mensual. Pequeñas optimizaciones en horarios y uso operativo pueden traducirse en ahorros importantes a mediano plazo.",

                    "La reducción del consumo energético no depende únicamente de instalar paneles solares ☀️. También influye bastante la forma en que se utilizan los equipos durante el día. Automatizar horarios, apagar áreas sin ocupación y evitar consumos innecesarios durante horas nocturnas puede disminuir considerablemente los costos operativos.\n\nMuchos negocios logran mejoras importantes simplemente reorganizando horarios de uso y controlando mejor las cargas eléctricas más exigentes.",

                    "Una buena gestión energética combina tecnología, planificación y hábitos de consumo eficientes 🌤️. Aprovechar correctamente la radiación solar disponible durante el día permite disminuir la dependencia de la red y optimizar el rendimiento energético general.\n\nAdemás, identificar los equipos que más consumen ayuda a tomar decisiones más inteligentes sobre horarios de operación y posibles mejoras futuras."
                ).random()
            }

            // =========================================
            // SALUDOS
            // =========================================

            "hola" in lower ||
                    "holaa" in lower ||
                    "buenas" in lower ||
                    "hello" in lower ||
                    "hi" in lower -> {

                listOf(

                    "¡Hola! 👋 Soy tu asistente energético inteligente ⚡. Puedo ayudarte con ahorro energético, paneles solares, consumo eléctrico, aire acondicionado, lavandería industrial y optimización de energía solar.",

                    "¡Hola! ☀️ Estoy aquí para ayudarte a optimizar el consumo energético de tu negocio y aprovechar mejor la energía solar disponible en Riohacha.",

                    "¡Bienvenido! ⚡ Puedes preguntarme sobre paneles solares, ahorro energético, facturas eléctricas, consumo de aire acondicionado y estrategias para reducir costos."
                ).random()
            }

            // =========================================
            // BUENOS DIAS
            // =========================================

            "buenos dias" in lower ||
                    "buen día" in lower ||
                    "buen dia" in lower -> {

                listOf(

                    "¡Buenos días! ☀️ Espero que tengas una excelente jornada. Recuerda que las horas de mayor radiación solar son ideales para aprovechar mejor la energía y reducir costos eléctricos.",

                    "¡Buenos días! ⚡ Estoy listo para ayudarte con recomendaciones sobre ahorro energético y optimización solar para tu negocio.",

                    "¡Muy buenos días! 🌤️ Este es un excelente momento para planificar consumos energéticos eficientes y aprovechar la producción solar."
                ).random()
            }

            // =========================================
            // BUENAS TARDES
            // =========================================

            "buenas tardes" in lower -> {

                listOf(

                    "¡Buenas tardes! ☀️ Todavía puedes aprovechar parte de la generación solar para optimizar consumos energéticos durante la tarde.",

                    "¡Buenas tardes! ⚡ Estoy aquí para ayudarte con estrategias de ahorro energético y eficiencia solar.",

                    "¡Buenas tardes! 🌤️ Si necesitas recomendaciones sobre consumo eléctrico o paneles solares puedo ayudarte."
                ).random()
            }

            // =========================================
            // BUENAS NOCHES
            // =========================================

            "buenas noches" in lower -> {

                listOf(

                    "¡Buenas noches! 🌙 Recuerda que durante horas nocturnas es importante reducir consumos innecesarios para optimizar el gasto energético.",

                    "¡Buenas noches! ⚡ Puedo ayudarte con recomendaciones para disminuir costos eléctricos y mejorar la eficiencia energética.",

                    "¡Buenas noches! 🌤️ Una buena planificación energética ayuda bastante a reducir el impacto del consumo nocturno."
                ).random()
            }

            // =========================================
            // GRACIAS
            // =========================================

            "gracias" in lower ||
                    "muchas gracias" in lower -> {

                listOf(

                    "¡Con gusto! ⚡ Estoy aquí para ayudarte a optimizar el consumo energético y aprovechar mejor la energía solar.",

                    "¡Siempre a la orden! ☀️ Si tienes más preguntas sobre ahorro energético o paneles solares puedes preguntarme.",

                    "¡Me alegra ayudarte! 🌤️ Recuerda que pequeñas optimizaciones energéticas pueden generar grandes ahorros."
                ).random()
            }

            // =========================================
            // QUIEN ERES
            // =========================================

            "quien eres" in lower ||
                    "quién eres" in lower ||
                    "que eres" in lower ||
                    "qué eres" in lower -> {

                listOf(

                    "Soy un asistente energético inteligente ⚡ especializado en optimización solar, ahorro energético y gestión eficiente del consumo eléctrico en negocios y empresas.",

                    "Soy tu copiloto energético ☀️. Puedo ayudarte con paneles solares, consumo eléctrico, aire acondicionado, facturas energéticas y estrategias de ahorro.",

                    "Soy un asistente de energía solar 🌤️ diseñado para ayudarte a tomar decisiones más inteligentes sobre consumo y eficiencia energética."
                ).random()
            }

            // =========================================
            // AYUDA
            // =========================================

            "ayuda" in lower ||
                    "puedes ayudarme" in lower ||
                    "que puedes hacer" in lower ||
                    "qué puedes hacer" in lower -> {

                listOf(

                    "Puedo ayudarte con recomendaciones sobre ahorro energético ⚡, paneles solares ☀️, optimización del aire acondicionado, lavandería industrial, reducción de facturas eléctricas y consumo eficiente.",

                    "Estoy diseñado para ayudarte a mejorar la eficiencia energética 🌤️ de negocios, hoteles y operaciones comerciales mediante estrategias solares inteligentes.",

                    "Puedo analizar temas relacionados con energía solar, consumo eléctrico y optimización energética para ayudarte a reducir costos operativos ⚡."
                ).random()
            }

            else -> {

                listOf(

                    "Lo siento ⚠️, esa pregunta parece estar fuera de mi área de especialización. Actualmente estoy enfocado en optimización energética, ahorro eléctrico y energía solar.\n\nPuedes preguntarme sobre consumo energético, paneles solares, aire acondicionado, facturas eléctricas o estrategias de ahorro para negocios.",

                    "No tengo suficiente contexto para responder correctamente esa consulta 🤖. Mi especialidad está relacionada con eficiencia energética, energía solar y optimización del consumo eléctrico.\n\nSi quieres, puedes hacerme otra pregunta relacionada con ahorro energético o gestión de energía.",

                    "Esa consulta parece alejarse de las funciones para las que fui diseñado ⚡. Estoy especializado en recomendaciones energéticas, consumo eléctrico y aprovechamiento solar.\n\nIntenta preguntarme sobre paneles solares, aire acondicionado, lavandería industrial o reducción de costos energéticos.",

                    "Actualmente no puedo darte una respuesta precisa sobre ese tema 🌤️. Mi enfoque principal es ayudarte con estrategias de eficiencia energética y optimización solar.\n\nEstoy disponible para responder preguntas relacionadas con consumo eléctrico, ahorro energético y operación eficiente de equipos.",

                    "Parece que esa pregunta está fuera de mi jurisdicción energética ☀️. Sin embargo, puedo ayudarte con recomendaciones relacionadas con paneles solares, consumo eléctrico, climatización y ahorro energético."
                ).random()
            }

        }
    }
}