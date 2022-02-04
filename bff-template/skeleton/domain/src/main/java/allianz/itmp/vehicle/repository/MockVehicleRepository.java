package allianz.itmp.vehicle.repository;

import allianz.itmp.mo.api.model.DataOption;
import allianz.itmp.mo.api.model.DataOptionList;
import org.springframework.stereotype.Component;

@Component
public class MockVehicleRepository implements VehicleRepository {

  @Override
  public DataOptionList vehicleBrands() {
    return new DataOptionList()
        .addValuesItem(
            new DataOption()
                .label("BMW")
                .value("BMW")
                .additionalText("")
        );
  }

}
