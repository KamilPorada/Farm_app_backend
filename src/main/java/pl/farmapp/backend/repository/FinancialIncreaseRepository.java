package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.FinancialIncrease;

import java.util.List;

@Repository
public interface FinancialIncreaseRepository
        extends JpaRepository<FinancialIncrease, Integer> {

    List<FinancialIncrease> findByFarmerId(Integer farmerId);

    List<FinancialIncrease> findByFinancialIncreaseTypeId(Integer typeId);
}
