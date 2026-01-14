package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.FinancialDecreaseType;

import java.util.List;

@Repository
public interface FinancialDecreaseTypeRepository
        extends JpaRepository<FinancialDecreaseType, Integer> {

    List<FinancialDecreaseType> findByFarmerId(Integer farmerId);
}
