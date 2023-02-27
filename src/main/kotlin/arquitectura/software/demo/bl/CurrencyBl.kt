package arquitectura.software.demo.bl

import arquitectura.software.demo.exception.ServiceException
import arquitectura.software.demo.dto.ErrorServiceDto
import arquitectura.software.demo.dto.ResponseServiceDto
import arquitectura.software.demo.dao.Currency
import arquitectura.software.demo.dao.Repository.CurrencyRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

@Service
class CurrencyBl(private val currencyRepository: CurrencyRepository) {

    companion object {
        val objectMapper = jacksonObjectMapper()
        val LOGGER: Logger = LoggerFactory.getLogger(CurrencyBl::class.java)
    }

    @Value("\${currency.url}")
    lateinit var apiUrl: String

    @Value("\${currency.key}")
    lateinit var apiKey: String

    fun exchangeRate(to: String, from: String, amount: BigDecimal): ResponseServiceDto {
        LOGGER.error("Iniciando logica para convertir divisas")
        if (amount < BigDecimal.ZERO) {
            LOGGER.error("El monto no puede ser negativo")
            throw IllegalArgumentException("El monto no puede ser negativo")
        }
        val response = invokeApi("$apiUrl?to=$to&from=$from&amount=$amount")
        val responseServiceDto = parseResponse(response)
        val currency = Currency()
        currency.currencyFrom = from
        currency.currencyTo = to
        currency.amount = amount
        currency.date = Date()
        currency.result = responseServiceDto.result
        currencyRepository.save(Currency(
            currencyFrom = currency.currencyFrom,
            currencyTo = currency.currencyTo,
            amount = currency.amount,
            date = currency.date,
            result = currency.result
        ))
        return responseServiceDto
    }

    fun invokeApi(endpoint: String): Response {
        LOGGER.info("Invocando servicio de conversión de monedas")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(endpoint)
            .addHeader("apikey", apiKey)
            .build()
        if (request != null) {
            LOGGER.info("El servicio de conversión de monedas fue exitoso")
            val currency: Currency = Currency()

        }
        try {
            return client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error en el servicio de conversión de monedas")
        }
    }

    fun parseResponse(response: Response): ResponseServiceDto {
        LOGGER.info("Parseando respuesta del servicio de conversión de monedas")
        val body = response.body().string()

        LOGGER.info("El servicio de conversión de monedas retorno => $body")
        if(response.isSuccessful) {
            LOGGER.info("El servicio de conversión de monedas fue exitoso")
            return objectMapper.readValue(body)
        }
        LOGGER.info("El servicio de conversión de monedas fue fallido")
        val errorService = objectMapper.readValue<ErrorServiceDto>(body)
        throw ServiceException("Code: ${errorService.error.code}, message: ${errorService.error.message}")
    }
}