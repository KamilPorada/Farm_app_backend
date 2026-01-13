package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Invoice;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.InvoiceRepository;
import pl.farmapp.backend.repository.PointOfSaleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final FarmerRepository farmerRepository;
    private final PointOfSaleRepository pointOfSaleRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          FarmerRepository farmerRepository,
                          PointOfSaleRepository pointOfSaleRepository) {
        this.invoiceRepository = invoiceRepository;
        this.farmerRepository = farmerRepository;
        this.pointOfSaleRepository = pointOfSaleRepository;
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getById(Integer id) {
        return invoiceRepository.findById(id);
    }

    public Optional<Invoice> create(Invoice invoice) {

        if (invoice.getFarmer() == null ||
                invoice.getFarmer().getId() == null ||
                !farmerRepository.existsById(invoice.getFarmer().getId())) {
            return Optional.empty();
        }

        if (invoice.getPointOfSale() == null ||
                invoice.getPointOfSale().getId() == null ||
                !pointOfSaleRepository.existsById(invoice.getPointOfSale().getId())) {
            return Optional.empty();
        }

        return Optional.of(invoiceRepository.save(invoice));
    }

    public Optional<Invoice> update(Integer id, Invoice updated) {
        return invoiceRepository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getPointOfSale() != null && updated.getPointOfSale().getId() != null) {
                if (!pointOfSaleRepository.existsById(updated.getPointOfSale().getId())) {
                    return Optional.empty();
                }
                existing.setPointOfSale(updated.getPointOfSale());
            }

            existing.setInvoiceDate(updated.getInvoiceDate());
            existing.setInvoiceNumber(updated.getInvoiceNumber());
            existing.setAmount(updated.getAmount());
            existing.setStatus(updated.getStatus());

            return Optional.of(invoiceRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        invoiceRepository.deleteById(id);
    }

    public List<Invoice> getByFarmer(Integer farmerId) {
        return invoiceRepository.findByFarmerId(farmerId);
    }

    public List<Invoice> getByFarmerAndStatus(Integer farmerId, Boolean status) {
        return invoiceRepository.findByFarmerIdAndStatus(farmerId, status);
    }
}
