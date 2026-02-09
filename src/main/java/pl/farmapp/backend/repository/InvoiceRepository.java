package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Invoice;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    List<Invoice> findByFarmerIdAndInvoiceDateBetween(
            Integer farmerId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Invoice> findByFarmerIdAndStatusAndInvoiceDateBetween(
            Integer farmerId,
            Boolean status,
            LocalDate startDate,
            LocalDate endDate
    );
}
