package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.InvoiceDto;
import pl.farmapp.backend.entity.Invoice;
import pl.farmapp.backend.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /* CREATE */
    public Invoice createInvoice(Integer farmerId, InvoiceDto dto) {
        Invoice invoice = new Invoice();
        invoice.setFarmerId(farmerId);
        invoice.setPointOfSaleId(dto.getPointOfSaleId());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setAmount(dto.getAmount());
        invoice.setStatus(false); // zawsze oczekująca

        return invoiceRepository.save(invoice);
    }

    /* UPDATE (pełna edycja) */
    public Invoice updateInvoice(Integer invoiceId, InvoiceDto dto) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setPointOfSaleId(dto.getPointOfSaleId());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setAmount(dto.getAmount());

        return invoiceRepository.save(invoice);
    }

    /* UPDATE STATUS (kluczowy endpoint) */
    public Invoice updateInvoiceStatus(Integer invoiceId, Boolean status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }

    /* DELETE */
    public void deleteInvoice(Integer invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    /* GET ALL BY FARMER + YEAR */
    public List<Invoice> getInvoicesByFarmerAndYear(Integer farmerId, int year) {
        return invoiceRepository.findByFarmerIdAndInvoiceDateBetween(
                farmerId,
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        );
    }

    /* GET REALIZED */
    public List<Invoice> getRealizedInvoices(Integer farmerId, int year) {
        return invoiceRepository.findByFarmerIdAndStatusAndInvoiceDateBetween(
                farmerId,
                true,
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        );
    }

    /* GET NOT REALIZED */
    public List<Invoice> getPendingInvoices(Integer farmerId, int year) {
        return invoiceRepository.findByFarmerIdAndStatusAndInvoiceDateBetween(
                farmerId,
                false,
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        );
    }
}
