package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import pl.farmapp.backend.dto.FarmerFullExportDto;
import pl.farmapp.backend.repository.*;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final FarmerRepository farmerRepository;
    private final FarmerDetailsRepository farmerDetailsRepository;
    private final CultivationCalendarRepository cultivationCalendarRepository;
    private final EmployeeRepository employeeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final FarmerTunnelsRepository farmerTunnelsRepository;
    private final FertigationRepository fertigationRepository;
    private final FertilizerRepository fertilizerRepository;
    private final FertilizerPriceRepository fertilizerPriceRepository;
    private final FinancialDecreaseRepository financialDecreaseRepository;
    private final FinancialDecreaseTypeRepository financialDecreaseTypeRepository;
    private final FinancialIncreaseRepository financialIncreaseRepository;
    private final FinancialIncreaseTypeRepository financialIncreaseTypeRepository;
    private final HarvestRepository harvestRepository;
    private final InvoiceRepository invoiceRepository;
    private final NoteRepository noteRepository;
    private final PesticideRepository pesticideRepository;
    private final PesticideTypeRepository pesticideTypeRepository;
    private final PointOfSaleRepository pointOfSaleRepository;
    private final TradeOfPepperRepository tradeOfPepperRepository;
    private final TreatmentRepository treatmentRepository;
    private final VarietySeasonRepository varietySeasonRepository;
    private final WorkTimeRepository workTimeRepository;
    private final MailService mailService;

    public ExportService(FarmerRepository farmerRepository, FarmerDetailsRepository farmerDetailsRepository, CultivationCalendarRepository cultivationCalendarRepository, EmployeeRepository employeeRepository, ExpenseRepository expenseRepository, ExpenseCategoryRepository expenseCategoryRepository, FarmerTunnelsRepository farmerTunnelsRepository, FertigationRepository fertigationRepository, FertilizerRepository fertilizerRepository, FertilizerPriceRepository fertilizerPriceRepository, FinancialDecreaseRepository financialDecreaseRepository, FinancialDecreaseTypeRepository financialDecreaseTypeRepository, FinancialIncreaseRepository financialIncreaseRepository, FinancialIncreaseTypeRepository financialIncreaseTypeRepository, HarvestRepository harvestRepository, InvoiceRepository invoiceRepository, NoteRepository noteRepository, PesticideRepository pesticideRepository, PesticideTypeRepository pesticideTypeRepository, PointOfSaleRepository pointOfSaleRepository, TradeOfPepperRepository tradeOfPepperRepository, TreatmentRepository treatmentRepository, VarietySeasonRepository varietySeasonRepository, WorkTimeRepository workTimeRepository, MailService mailService) {
        this.farmerRepository = farmerRepository;
        this.farmerDetailsRepository = farmerDetailsRepository;
        this.cultivationCalendarRepository = cultivationCalendarRepository;
        this.employeeRepository = employeeRepository;
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.farmerTunnelsRepository = farmerTunnelsRepository;
        this.fertigationRepository = fertigationRepository;
        this.fertilizerRepository = fertilizerRepository;
        this.fertilizerPriceRepository = fertilizerPriceRepository;
        this.financialDecreaseRepository = financialDecreaseRepository;
        this.financialDecreaseTypeRepository = financialDecreaseTypeRepository;
        this.financialIncreaseRepository = financialIncreaseRepository;
        this.financialIncreaseTypeRepository = financialIncreaseTypeRepository;
        this.harvestRepository = harvestRepository;
        this.invoiceRepository = invoiceRepository;
        this.noteRepository = noteRepository;
        this.pesticideRepository = pesticideRepository;
        this.pesticideTypeRepository = pesticideTypeRepository;
        this.pointOfSaleRepository = pointOfSaleRepository;
        this.tradeOfPepperRepository = tradeOfPepperRepository;
        this.treatmentRepository = treatmentRepository;
        this.varietySeasonRepository = varietySeasonRepository;
        this.workTimeRepository = workTimeRepository;
        this.mailService = mailService;
    }

    public FarmerFullExportDto exportFarmerData(Integer farmerId) {

        FarmerFullExportDto dto = new FarmerFullExportDto();

        dto.setFarmer(farmerRepository.findById(farmerId).orElseThrow());
        dto.setFarmerDetails(
                farmerDetailsRepository.findByFarmerId(farmerId).orElse(null)
        );        dto.setCultivationCalendars(cultivationCalendarRepository.findByFarmerId(farmerId));
        dto.setEmployees(employeeRepository.findByFarmerId(farmerId));
        dto.setExpenses(expenseRepository.findByFarmerId(farmerId));
        dto.setExpenseCategories(expenseCategoryRepository.findByFarmerId(farmerId));
        dto.setFarmerTunnels(farmerTunnelsRepository.findByFarmerId(farmerId));
        dto.setFertigations(fertigationRepository.findByFarmerId(farmerId));
        dto.setFertilizers(fertilizerRepository.findByFarmerId(farmerId));
        dto.setFertilizerPrices(fertilizerPriceRepository.findByFarmerId(farmerId));
        dto.setFinancialDecreases(financialDecreaseRepository.findByFarmerId(farmerId));
        dto.setFinancialDecreaseTypes(financialDecreaseTypeRepository.findByFarmerId(farmerId));
        dto.setFinancialIncreases(financialIncreaseRepository.findByFarmerId(farmerId));
        dto.setFinancialIncreaseTypes(financialIncreaseTypeRepository.findByFarmerId(farmerId));
        dto.setHarvests(harvestRepository.findByFarmerId(farmerId));
        dto.setInvoices(invoiceRepository.findByFarmerId(farmerId));
        dto.setNotes(noteRepository.findByFarmerId(farmerId));
        dto.setPesticides(pesticideRepository.findByFarmerId(farmerId));
        dto.setPesticideTypes(pesticideTypeRepository.findByFarmerId(farmerId));
        dto.setPointsOfSale(pointOfSaleRepository.findByFarmerId(farmerId));
        dto.setTradesOfPepper(tradeOfPepperRepository.findByFarmerId(farmerId));
        dto.setTreatments(treatmentRepository.findByFarmerId(farmerId));
        dto.setVarietySeasons(varietySeasonRepository.findByFarmerId(farmerId));
        dto.setWorkTimes(workTimeRepository.findByFarmerId(farmerId));

        return dto;
    }

    public String generateJson(Integer farmerId) throws Exception {

        FarmerFullExportDto exportDTO = exportFarmerData(farmerId);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(exportDTO);
    }

    public void sendExportToEmail(Integer farmerId) throws Exception {

        FarmerFullExportDto exportDTO = exportFarmerData(farmerId);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(exportDTO);

        String email = exportDTO.getFarmer().getEmail();

        mailService.sendBackupEmail(email, json);
    }
}