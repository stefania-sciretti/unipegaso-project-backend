package com.clinica.application.domain

enum class GlycemiaContext {
    A_DIGIUNO,
    POST_PASTO_1H,
    POST_PASTO_2H;
}

enum class GlycemiaClassification {
    NORMALE,
    ATTENZIONE,
    ELEVATA;
}

fun computeClassification(context: GlycemiaContext, valueMgDl: Int): GlycemiaClassification =
    when (context) {
        GlycemiaContext.A_DIGIUNO -> when {
            valueMgDl < 100  -> GlycemiaClassification.NORMALE
            valueMgDl < 126  -> GlycemiaClassification.ATTENZIONE
            else             -> GlycemiaClassification.ELEVATA
        }
        GlycemiaContext.POST_PASTO_1H -> when {
            valueMgDl < 155  -> GlycemiaClassification.NORMALE
            valueMgDl < 209  -> GlycemiaClassification.ATTENZIONE
            else             -> GlycemiaClassification.ELEVATA
        }
        GlycemiaContext.POST_PASTO_2H -> when {
            valueMgDl < 140  -> GlycemiaClassification.NORMALE
            valueMgDl < 200  -> GlycemiaClassification.ATTENZIONE
            else             -> GlycemiaClassification.ELEVATA
        }
    }

