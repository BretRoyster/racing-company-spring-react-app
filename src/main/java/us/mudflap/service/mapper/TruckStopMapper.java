package us.mudflap.service.mapper;

import us.mudflap.domain.*;
import us.mudflap.service.dto.TruckStopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TruckStop} and its DTO {@link TruckStopDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TruckStopMapper extends EntityMapper<TruckStopDTO, TruckStop> {



    default TruckStop fromId(Long id) {
        if (id == null) {
            return null;
        }
        TruckStop truckStop = new TruckStop();
        truckStop.setId(id);
        return truckStop;
    }
}
