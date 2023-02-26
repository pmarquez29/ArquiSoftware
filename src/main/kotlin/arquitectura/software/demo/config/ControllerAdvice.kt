package arquitectura.software.demo.config


import arquitectura.software.demo.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.SocketTimeoutException
import java.time.LocalDateTime


@ControllerAdvice
class ControllerAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(ex: ServiceException): ResponseEntity<Any?>? {
        val body: MutableMap<String, Any?> = LinkedHashMap()
        body["timestamp"] = LocalDateTime.now()
        body["message"] = ex.message
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(SocketTimeoutException::class)
    fun handleSocketTimeoutException(ex: SocketTimeoutException): ResponseEntity<Any?>? {
        val body: MutableMap<String, Any?> = LinkedHashMap()
        body["timestamp"] = LocalDateTime.now()
        body["message"] = "El servicio no se encuentran disponibles"
        return ResponseEntity(body, HttpStatus.SERVICE_UNAVAILABLE)
    }
}