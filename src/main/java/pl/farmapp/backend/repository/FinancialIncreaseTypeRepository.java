package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.FinancialIncreaseType;

import java.util.List;

@Repository
public interface FinancialIncreaseTypeRepository
        extends JpaRepository<FinancialIncreaseType, Integer> {

    List<FinancialIncreaseType> findByFarmerId(Integer farmerId);
}
