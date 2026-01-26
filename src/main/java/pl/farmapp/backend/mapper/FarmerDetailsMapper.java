package pl.farmapp.backend.mapper;


import pl.farmapp.backend.dto.FarmerDetailsDto;
import pl.farmapp.backend.entity.FarmerDetails;

import java.util.List;

public class FarmerDetailsMapper {

    public static FarmerDetails toEntity(FarmerDetailsDto dto) {
        FarmerDetails e = new FarmerDetails();
        e.setFarmerId(dto.farmerId);
        e.setVoivodeship(dto.voivodeship);
        e.setDistrict(dto.district);
        e.setCommune(dto.commune);
        e.setLocality(dto.locality);
        e.setFarmAreaHa(dto.farmAreaHa);
        e.setSettlementType(dto.settlementType);
        e.setCrops(dto.crops != null ? String.join(",", dto.crops) : null);
        return e;
    }

    public static FarmerDetailsDto toDto(FarmerDetails e) {
        FarmerDetailsDto dto = new FarmerDetailsDto();
        dto.farmerId = e.getFarmerId();
        dto.voivodeship = e.getVoivodeship();
        dto.district = e.getDistrict();
        dto.commune = e.getCommune();
        dto.locality = e.getLocality();
        dto.farmAreaHa = e.getFarmAreaHa();
        dto.settlementType = e.getSettlementType();
        dto.crops = e.getCrops();
        return dto;
    }
}
