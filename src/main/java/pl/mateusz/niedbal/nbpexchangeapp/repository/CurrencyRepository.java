package pl.mateusz.niedbal.nbpexchangeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Finds {@link Currency} in repository by its code and date with jpa query
     * @param code LocalDate
     * @param date 3 letters String code
     * @return Optional of {@link Currency}
     */
    @Query("select c from Currency c where lower(c.code) = lower(:code) and c.date = :date")
    Optional<Currency> findByCode(@Param("code") String code, @Param("date") LocalDate date);
}
