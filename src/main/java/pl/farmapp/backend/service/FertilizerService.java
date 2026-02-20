package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FertilizerDto;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.repository.FertilizerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FertilizerService {

    private final FertilizerRepository repository;

    public FertilizerService(FertilizerRepository repository) {
        this.repository = repository;
    }

    public Fertilizer create(Integer farmerId, FertilizerDto dto) {
        Fertilizer fertilizer = new Fertilizer();
        fertilizer.setFarmerId(farmerId);
        fertilizer.setName(dto.getName());
        fertilizer.setForm(dto.getForm());

        return repository.save(fertilizer);
    }

    public Fertilizer update(Integer id, FertilizerDto dto) {
        Fertilizer fertilizer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found"));

        fertilizer.setName(dto.getName());
        fertilizer.setForm(dto.getForm());

        return repository.save(fertilizer);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Fertilizer> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}