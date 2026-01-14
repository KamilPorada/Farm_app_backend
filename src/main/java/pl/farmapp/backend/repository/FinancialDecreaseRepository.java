package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.FinancialDecrease;

import java.util.List;

@Repository
public interface FinancialDecreaseRepository
        extends JpaRepository<FinancialDecrease, Integer> {

    List<FinancialDecrease> findByFarmerId(Integer farmerId);

    List<FinancialDecrease> findByFinancialDecreaseTypeId(Integer typeId);
}
