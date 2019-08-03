package us.rabtrade.service.mapper;

import us.rabtrade.domain.*;
import us.rabtrade.service.dto.RacingCompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RacingCompany} and its DTO {@link RacingCompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RacingCompanyMapper extends EntityMapper<RacingCompanyDTO, RacingCompany> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    RacingCompanyDTO toDto(RacingCompany racingCompany);

    @Mapping(source = "ownerId", target = "owner")
    RacingCompany toEntity(RacingCompanyDTO racingCompanyDTO);

    default RacingCompany fromId(Long id) {
        if (id == null) {
            return null;
        }
        RacingCompany racingCompany = new RacingCompany();
        racingCompany.setId(id);
        return racingCompany;
    }
}
