package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Invoice;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    List<Invoice> findByFarmerId(Integer farmerId);

    List<Invoice> findByPointOfSaleId(Integer pointOfSaleId);

    List<Invoice> findByFarmerIdAndStatus(Integer farmerId, Boolean status);

    List<Invoice> findByFarmerIdAndInvoiceDateBetween(
            Integer farmerId,
            LocalDate from,
            LocalDate to
    );
}
