package pl.farmapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.farmapp.backend.entity.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FarmerFullExportDto {

    private Farmer farmer;
    private FarmerDetails farmerDetails;
    private AppSettings appSettings;
    private List<CultivationCalendar> cultivationCalendars;
    private List<Employee> employees;
    private List<Expense> expenses;
    private List<ExpenseCategory> expenseCategories;
    private List<FarmerTunnels> farmerTunnels;
    private List<Fertigation> fertigations;
    private List<Fertilizer> fertilizers;
    private List<FertilizerPrice> fertilizerPrices;
    private List<FinancialDecrease> financialDecreases;
    private List<FinancialDecreaseType> financialDecreaseTypes;
    private List<FinancialIncrease> financialIncreases;
    private List<FinancialIncreaseType> financialIncreaseTypes;
    private List<Harvest> harvests;
    private List<Invoice> invoices;
    private List<Note> notes;
    private List<Pesticide> pesticides;
    private List<PesticideType> pesticideTypes;
    private List<PointOfSale> pointsOfSale;
    private List<TradeOfPepper> tradesOfPepper;
    private List<Treatment> treatments;
    private List<VarietySeason> varietySeasons;
    private List<WorkTime> workTimes;

    // GETTERY I SETTERY

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

    public FarmerDetails getFarmerDetails() { return farmerDetails; }
    public void setFarmerDetails(FarmerDetails farmerDetails) { this.farmerDetails = farmerDetails; }

    public AppSettings getAppSettings() { return appSettings; }
    public void setAppSettings(AppSettings appSettings) { this.appSettings = appSettings; }

    public List<CultivationCalendar> getCultivationCalendars() { return cultivationCalendars; }
    public void setCultivationCalendars(List<CultivationCalendar> cultivationCalendars) { this.cultivationCalendars = cultivationCalendars; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }

    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }

    public List<ExpenseCategory> getExpenseCategories() { return expenseCategories; }
    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) { this.expenseCategories = expenseCategories; }

    public List<FarmerTunnels> getFarmerTunnels() { return farmerTunnels; }
    public void setFarmerTunnels(List<FarmerTunnels> farmerTunnels) { this.farmerTunnels = farmerTunnels; }

    public List<Fertigation> getFertigations() { return fertigations; }
    public void setFertigations(List<Fertigation> fertigations) { this.fertigations = fertigations; }

    public List<Fertilizer> getFertilizers() { return fertilizers; }
    public void setFertilizers(List<Fertilizer> fertilizers) { this.fertilizers = fertilizers; }

    public List<FertilizerPrice> getFertilizerPrices() { return fertilizerPrices; }
    public void setFertilizerPrices(List<FertilizerPrice> fertilizerPrices) { this.fertilizerPrices = fertilizerPrices; }

    public List<FinancialDecrease> getFinancialDecreases() { return financialDecreases; }
    public void setFinancialDecreases(List<FinancialDecrease> financialDecreases) { this.financialDecreases = financialDecreases; }

    public List<FinancialDecreaseType> getFinancialDecreaseTypes() { return financialDecreaseTypes; }
    public void setFinancialDecreaseTypes(List<FinancialDecreaseType> financialDecreaseTypes) { this.financialDecreaseTypes = financialDecreaseTypes; }

    public List<FinancialIncrease> getFinancialIncreases() { return financialIncreases; }
    public void setFinancialIncreases(List<FinancialIncrease> financialIncreases) { this.financialIncreases = financialIncreases; }

    public List<FinancialIncreaseType> getFinancialIncreaseTypes() { return financialIncreaseTypes; }
    public void setFinancialIncreaseTypes(List<FinancialIncreaseType> financialIncreaseTypes) { this.financialIncreaseTypes = financialIncreaseTypes; }

    public List<Harvest> getHarvests() { return harvests; }
    public void setHarvests(List<Harvest> harvests) { this.harvests = harvests; }

    public List<Invoice> getInvoices() { return invoices; }
    public void setInvoices(List<Invoice> invoices) { this.invoices = invoices; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    public List<Pesticide> getPesticides() { return pesticides; }
    public void setPesticides(List<Pesticide> pesticides) { this.pesticides = pesticides; }

    public List<PesticideType> getPesticideTypes() { return pesticideTypes; }
    public void setPesticideTypes(List<PesticideType> pesticideTypes) { this.pesticideTypes = pesticideTypes; }

    public List<PointOfSale> getPointsOfSale() { return pointsOfSale; }
    public void setPointsOfSale(List<PointOfSale> pointsOfSale) { this.pointsOfSale = pointsOfSale; }

    public List<TradeOfPepper> getTradesOfPepper() { return tradesOfPepper; }
    public void setTradesOfPepper(List<TradeOfPepper> tradesOfPepper) { this.tradesOfPepper = tradesOfPepper; }

    public List<Treatment> getTreatments() { return treatments; }
    public void setTreatments(List<Treatment> treatments) { this.treatments = treatments; }

    public List<VarietySeason> getVarietySeasons() { return varietySeasons; }
    public void setVarietySeasons(List<VarietySeason> varietySeasons) { this.varietySeasons = varietySeasons; }

    public List<WorkTime> getWorkTimes() { return workTimes; }
    public void setWorkTimes(List<WorkTime> workTimes) { this.workTimes = workTimes; }
}