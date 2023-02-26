package arquitectura.software.demo.dto

import java.math.BigDecimal

data class RequestServiceDto (
    val from: String,
    val to: String,
    val amount: BigDecimal
)