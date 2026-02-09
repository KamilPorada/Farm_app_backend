package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.InvoiceDto;
import pl.farmapp.backend.entity.Invoice;
import pl.farmapp.backend.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /* CREATE */
    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<Invoice> createInvoice(
            @PathVariable Integer farmerId,
            @RequestBody InvoiceDto dto
    ) {
        return ResponseEntity.ok(invoiceService.createInvoice(farmerId, dto));
    }

    /* UPDATE */
    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable Integer invoiceId,
            @RequestBody InvoiceDto dto
    ) {
        return ResponseEntity.ok(invoiceService.updateInvoice(invoiceId, dto));
    }

    /* UPDATE STATUS – KLUCZOWY */
    @PatchMapping("/{invoiceId}/status")
    public ResponseEntity<Invoice> updateInvoiceStatus(
            @PathVariable Integer invoiceId,
            @RequestParam Boolean status
    ) {
        return ResponseEntity.ok(
                invoiceService.updateInvoiceStatus(invoiceId, status)
        );
    }

    /* DELETE */
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Integer invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }

    /* GET ALL */
    @GetMapping("/farmer/{farmerId}/year/{year}")
    public ResponseEntity<List<Invoice>> getInvoices(
            @PathVariable Integer farmerId,
            @PathVariable int year
    ) {
        return ResponseEntity.ok(
                invoiceService.getInvoicesByFarmerAndYear(farmerId, year)
        );
    }

    /* GET REALIZED */
    @GetMapping("/farmer/{farmerId}/year/{year}/realized")
    public ResponseEntity<List<Invoice>> getRealizedInvoices(
            @PathVariable Integer farmerId,
            @PathVariable int year
    ) {
        return ResponseEntity.ok(
                invoiceService.getRealizedInvoices(farmerId, year)
        );
    }

    /* GET PENDING */
    @GetMapping("/farmer/{farmerId}/year/{year}/pending")
    public ResponseEntity<List<Invoice>> getPendingInvoices(
            @PathVariable Integer farmerId,
            @PathVariable int year
    ) {
        return ResponseEntity.ok(
                invoiceService.getPendingInvoices(farmerId, year)
        );
    }
}
