package arquitectura.software.demo.dao.Repository

import arquitectura.software.demo.dao.Currency
import org.springframework.data.repository.CrudRepository

interface CurrencyRepository: CrudRepository<Currency, Long>