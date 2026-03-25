package pl.farmapp.backend.service.report;

import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.*;
import pl.farmapp.backend.repository.*;
import pl.farmapp.backend.service.report.pages.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SeasonReportService {

    private final TitlePageGenerator titlePageGenerator;
    private final TableOfContentsGenerator tableOfContentsGenerator;
    private final SeasonBasicInfoPageGenerator seasonBasicInfoPageGenerator;
    private final CropStructurePageGenerator cropStructurePageGenerator;
    private final HarvestSummaryPageGenerator harvestSummaryPageGenerator;
    private final PepperSalesPageGenerator pepperSalesPageGenerator;
    private final ExpensesSectionGenerator expensesSectionGenerator;

    private final SeasonalWorkersSectionGenerator seasonalWorkersSectionGenerator;
    private final PlantProtectionSectionGenerator plantProtectionSectionGenerator;
    private final FertigationSectionGenerator fertigationSectionGenerator;
    private final NotesSectionGenerator notesSectionGenerator; // 🔥 NOWE
    private final ProfitabilitySectionGenerator profitabilitySectionGenerator; // 🔥 NOWE


    private final FarmerRepository farmerRepository;
    private final FarmerDetailsRepository farmerDetailsRepository;
    private final CultivationCalendarRepository cultivationCalendarRepository;
    private final FarmerTunnelsRepository farmerTunnelsRepository;
    private final VarietySeasonRepository varietySeasonRepository;
    private final HarvestRepository harvestRepository;
    private final AppSettingsRepository appSettingsRepository;
    private final TradeOfPepperRepository tradeOfPepperRepository;
    private final PointOfSaleRepository pointOfSaleRepository;

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    private final TreatmentRepository treatmentRepository;
    private final PesticideRepository pesticideRepository;
    private final PesticideTypeRepository pesticideTypeRepository;

    private final EmployeeRepository employeeRepository;
    private final WorkTimeRepository workTimeRepository;

    private final FertigationRepository fertigationRepository;
    private final FertilizerRepository fertilizerRepository;
    private final FertilizerPriceRepository fertilizerPriceRepository;

    private final NoteRepository noteRepository; // 🔥 NOWE

    public SeasonReportService(
            TitlePageGenerator titlePageGenerator,
            TableOfContentsGenerator tableOfContentsGenerator,
            SeasonBasicInfoPageGenerator seasonBasicInfoPageGenerator,
            CropStructurePageGenerator cropStructurePageGenerator,
            HarvestSummaryPageGenerator harvestSummaryPageGenerator,
            PepperSalesPageGenerator pepperSalesPageGenerator,
            ExpensesSectionGenerator expensesSectionGenerator,
            SeasonalWorkersSectionGenerator seasonalWorkersSectionGenerator,
            PlantProtectionSectionGenerator plantProtectionSectionGenerator,
            FertigationSectionGenerator fertigationSectionGenerator,
            NotesSectionGenerator notesSectionGenerator, // 🔥 NOWE

            ProfitabilitySectionGenerator profitabilitySectionGenerator, FarmerRepository farmerRepository, FarmerDetailsRepository farmerDetailsRepository,
            CultivationCalendarRepository cultivationCalendarRepository,
            FarmerTunnelsRepository farmerTunnelsRepository,
            VarietySeasonRepository varietySeasonRepository,
            HarvestRepository harvestRepository,
            AppSettingsRepository appSettingsRepository,
            TradeOfPepperRepository tradeOfPepperRepository,
            PointOfSaleRepository pointOfSaleRepository,
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository expenseCategoryRepository,
            TreatmentRepository treatmentRepository,
            PesticideRepository pesticideRepository,
            PesticideTypeRepository pesticideTypeRepository,
            EmployeeRepository employeeRepository,
            WorkTimeRepository workTimeRepository,

            FertigationRepository fertigationRepository,
            FertilizerRepository fertilizerRepository,
            FertilizerPriceRepository fertilizerPriceRepository,
            NoteRepository noteRepository // 🔥 NOWE
    ) {
        this.titlePageGenerator = titlePageGenerator;
        this.tableOfContentsGenerator = tableOfContentsGenerator;
        this.seasonBasicInfoPageGenerator = seasonBasicInfoPageGenerator;
        this.cropStructurePageGenerator = cropStructurePageGenerator;
        this.harvestSummaryPageGenerator = harvestSummaryPageGenerator;
        this.pepperSalesPageGenerator = pepperSalesPageGenerator;
        this.expensesSectionGenerator = expensesSectionGenerator;

        this.seasonalWorkersSectionGenerator = seasonalWorkersSectionGenerator;
        this.plantProtectionSectionGenerator = plantProtectionSectionGenerator;
        this.fertigationSectionGenerator = fertigationSectionGenerator;
        this.notesSectionGenerator = notesSectionGenerator;
        this.profitabilitySectionGenerator = profitabilitySectionGenerator;
        this.farmerRepository = farmerRepository;

        this.farmerDetailsRepository = farmerDetailsRepository;
        this.cultivationCalendarRepository = cultivationCalendarRepository;
        this.farmerTunnelsRepository = farmerTunnelsRepository;
        this.varietySeasonRepository = varietySeasonRepository;
        this.harvestRepository = harvestRepository;
        this.appSettingsRepository = appSettingsRepository;
        this.tradeOfPepperRepository = tradeOfPepperRepository;
        this.pointOfSaleRepository = pointOfSaleRepository;

        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.treatmentRepository = treatmentRepository;
        this.pesticideRepository = pesticideRepository;
        this.pesticideTypeRepository = pesticideTypeRepository;

        this.employeeRepository = employeeRepository;
        this.workTimeRepository = workTimeRepository;

        this.fertigationRepository = fertigationRepository;
        this.fertilizerRepository = fertilizerRepository;
        this.fertilizerPriceRepository = fertilizerPriceRepository;

        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public byte[] generateSeasonReport(Integer farmerId, Integer year) {
        try {
            XWPFDocument document = new XWPFDocument();
            document.enforceUpdateFields();
            Farmer farmer = farmerRepository.findById(farmerId).orElseThrow(() -> new RuntimeException("Brak danych użytkownika"));
            String name = farmer.getName();
            String surname = farmer.getSurname();
            FarmerDetails details = farmerDetailsRepository
                    .findByFarmerId(farmerId)
                    .orElseThrow(() -> new RuntimeException("Brak danych gospodarstwa"));
            System.out.println(farmerId);

            String locality = details.getLocality();
            String commune = details.getCommune();

            CultivationCalendar calendar = cultivationCalendarRepository
                    .findByFarmerIdAndSeasonYear(farmerId, year)
                    .orElseThrow(() -> new RuntimeException("Brak kalendarza sezonu"));

            Double tunnelCountCurrent = farmerTunnelsRepository
                    .findByFarmerIdAndYear(farmerId, year)
                    .map(t -> t.getTunnelsCount().doubleValue())
                    .orElse(1.0);

            Double tunnelCountPrevious = farmerTunnelsRepository
                    .findByFarmerIdAndYear(farmerId, year - 1)
                    .map(t -> t.getTunnelsCount().doubleValue())
                    .orElse(null);

            List<VarietySeason> varieties = varietySeasonRepository
                    .findByFarmerIdAndSeasonYear(farmerId, year);

            List<Harvest> harvests = harvestRepository
                    .findByFarmerIdAndYear(farmerId, year);

            Double boxWeightKg = appSettingsRepository
                    .findByFarmerId(farmerId)
                    .map(s -> s.getBoxWeightKg().doubleValue())
                    .orElse(220.0);

            List<TradeOfPepper> trades = tradeOfPepperRepository
                    .findByFarmerIdAndYear(farmerId, year);
            List<TradeOfPepper> tradesPrev = tradeOfPepperRepository
                    .findByFarmerIdAndYear(farmerId, year - 1);

            List<PointOfSale> pointsOfSale = pointOfSaleRepository
                    .findByFarmerId(farmerId);

            List<Expense> expenses = expenseRepository.findByFarmerId(farmerId).stream()
                    .filter(e -> e.getExpenseDate().getYear() == year)
                    .toList();

            List<ExpenseCategory> categories = expenseCategoryRepository
                    .findByFarmerId(farmerId).stream()
                    .filter(c -> c.getSeasonYear().equals(year))
                    .toList();

            List<Employee> employees = employeeRepository
                    .findByFarmerIdAndSeasonYear(farmerId, year);

            List<WorkTime> workTimes = workTimeRepository.findByFarmerId(farmerId)
                    .stream()
                    .filter(wt -> wt.getWorkDate().getYear() == year)
                    .toList();

            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = LocalDate.of(year, 12, 31);

            List<Treatment> treatments = treatmentRepository
                    .findAllByFarmerIdAndTreatmentDateBetween(farmerId, start, end);

            List<Pesticide> pesticides = pesticideRepository.findByFarmerId(farmerId);
            List<PesticideType> pesticideTypes = pesticideTypeRepository.findByFarmerId(farmerId);

            List<Fertigation> fertigations = fertigationRepository
                    .findByFarmerIdAndFertigationDateBetween(farmerId, start, end);

            List<Fertilizer> fertilizers = fertilizerRepository.findByFarmerId(farmerId);

            List<FertilizerPrice> fertilizerPrices = fertilizerPriceRepository
                    .findByFarmerId(farmerId);

            Map<Integer, BigDecimal> priceMap = fertilizerPrices.stream()
                    .filter(p -> p.getSeasonYear().equals(year))
                    .collect(Collectors.toMap(
                            FertilizerPrice::getFertilizerId,
                            FertilizerPrice::getPricePerUnit
                    ));

            // 🔥 NOTATKI
            List<Note> notes = noteRepository.findByFarmerId(farmerId).stream()
                    .filter(n -> n.getNoteDate().getYear() == year)
                    .toList();

            // ===== GENEROWANIE =====
            titlePageGenerator.generate(document, year, locality, commune, name, surname);
            tableOfContentsGenerator.generate(document);
            seasonBasicInfoPageGenerator.generate(document, calendar, tunnelCountCurrent, tunnelCountPrevious);
            cropStructurePageGenerator.generate(document, varieties, year);
            harvestSummaryPageGenerator.generate(document, calendar, harvests, varieties, boxWeightKg);
            pepperSalesPageGenerator.generate(document, trades, pointsOfSale, year, tunnelCountCurrent);

            int tableIndex = 10;

            tableIndex = expensesSectionGenerator.generate(
                    document, year, expenses, categories, employees, workTimes, tableIndex
            );

            tableIndex = seasonalWorkersSectionGenerator.generate(
                    document, year, employees, workTimes, tableIndex
            );

            tableIndex = plantProtectionSectionGenerator.generate(
                    document, year, treatments, pesticides, pesticideTypes, tableIndex
            );

            tableIndex = fertigationSectionGenerator.generate(
                    document,
                    year,
                    fertigations,
                    fertilizers,
                    priceMap,
                    tunnelCountCurrent,
                    tableIndex
            );

            // 🔥 NOWA SEKCJA – NOTATKI
            notesSectionGenerator.generate(
                    document,
                    year,
                    notes
            );
            profitabilitySectionGenerator.generate(
                    document,
                    year,
                    trades,
                    tradesPrev,
                    pointsOfSale,
                    expenses,
                    categories,
                    workTimes,
                    tunnelCountCurrent,
                    tunnelCountPrevious,
                    calendar.getPrickingStartDate(),   // ✅ LocalDate
                    calendar.getHarvestEndDate()       // ✅ LocalDate
            );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.write(out);
            byte[] reportBytes = out.toByteArray();

            return reportBytes;

        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania raportu", e);
        }
    }
}