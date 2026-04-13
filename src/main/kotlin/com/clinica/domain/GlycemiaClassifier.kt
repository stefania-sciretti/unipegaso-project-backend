package com.clinica.domain

import java.math.BigDecimal

/**
 * Value object responsible for classifying a glycemia reading according to
 * WHO/ADA guidelines (Single Responsibility Principle).
 *
 * Previously embedded inside [com.clinica.service.GlycemiaMeasurementService];
 * moving it here separates domain logic from persistence concerns.
 */
object GlycemiaClassifier {

    /**
     * Returns the risk classification for a given [valueMgDl] and measurement [context].
     *
     * @param valueMgDl glucose value in mg/dL
     * @param context   one of: FASTING, POST_MEAL_1H, POST_MEAL_2H, RANDOM
     * @return          NORMALE | ATTENZIONE | ELEVATA
     */
    fun classify(valueMgDl: BigDecimal, context: String): String {
        val value = valueMgDl.toDouble()
        return when (context) {
            "FASTING"      -> classifyFasting(value)
            "POST_MEAL_1H" -> classifyPostMeal1h(value)
            "POST_MEAL_2H" -> classifyPostMeal2h(value)
            else           -> classifyRandom(value)
        }
    }

    private fun classifyFasting(value: Double) = when {
        value < 100 -> "NORMALE"
        value < 126 -> "ATTENZIONE"
        else        -> "ELEVATA"
    }

    private fun classifyPostMeal1h(value: Double) = when {
        value < 140 -> "NORMALE"
        value < 180 -> "ATTENZIONE"
        else        -> "ELEVATA"
    }

    private fun classifyPostMeal2h(value: Double) = when {
        value < 140 -> "NORMALE"
        value < 200 -> "ATTENZIONE"
        else        -> "ELEVATA"
    }

    private fun classifyRandom(value: Double) = when {
        value < 140 -> "NORMALE"
        value < 200 -> "ATTENZIONE"
        else        -> "ELEVATA"
    }
}
