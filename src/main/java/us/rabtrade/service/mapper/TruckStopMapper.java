package us.rabtrade.service.mapper;

import us.rabtrade.domain.*;
import us.rabtrade.service.dto.TruckStopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TruckStop} and its DTO {@link TruckStopDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TruckStopMapper extends EntityMapper<TruckStopDTO, TruckStop> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    TruckStopDTO toDto(TruckStop truckStop);

    @Mapping(source = "ownerId", target = "owner")
    TruckStop toEntity(TruckStopDTO truckStopDTO);

    default TruckStop fromId(Long id) {
        if (id == null) {
            return null;
        }
        TruckStop truckStop = new TruckStop();
        truckStop.setId(id);
        return truckStop;
    }
}
