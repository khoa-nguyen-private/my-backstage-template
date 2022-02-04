package allianz.itmp.vehicle.service;

import allianz.itmp.mo.api.model.DataOptionList;
import allianz.itmp.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;


  @Override
  public DataOptionList vehicleBrands() {
    return vehicleRepository.vehicleBrands();
  }

}
