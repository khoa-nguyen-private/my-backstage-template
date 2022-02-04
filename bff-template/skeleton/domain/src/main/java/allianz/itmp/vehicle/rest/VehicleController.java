package allianz.itmp.vehicle.rest;

import allianz.itmp.mo.api.VehicleSearchApi;
import allianz.itmp.mo.api.model.DataOptionList;

import allianz.itmp.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehicleSearchApi {

  private final VehicleService vehicleService;


  @Override
  public ResponseEntity<DataOptionList> vehicleBrands() {
    DataOptionList brands = vehicleService.vehicleBrands();
    return ResponseEntity
        .ok()
        .body(brands);
  }
}
