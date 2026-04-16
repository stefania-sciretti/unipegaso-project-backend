package com.clinica.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("CentroFitness Simona & Luca – API")
                    .version("1.0.0")
                    .description(
                        """
                        ## API REST – CentroFitness di Simona e Luca
                        
                        - **Clients**: anagrafica clienti del centro
                        - **Trainers**: Simona (Nutrizionista) e Luca (Personal Trainer)
                        - **Appointments**: prenotazioni per consulenze/allenamenti
                        - **Diet Plans**: piani dietetici personalizzati (Simona)
                        - **Training Plans**: schede di allenamento (Luca)
                        - **Recipes**: ricette fit (Simona)
                        - **Dashboard**: statistiche generali
                        
                        ### Stati Appuntamento
                        - `BOOKED` → prenotato
                        - `CONFIRMED` → confermato
                        - `COMPLETED` → completato
                        - `CANCELLED` → annullato
                        """.trimIndent()
                    )
                    .contact(
                        Contact()
                            .name("CentroFitness Simona & Luca")
                            .email("info@centrofitness.it")
                    )
            )
    }
}
