package ftn.ratingservice.domain.mappers;

import ftn.ratingservice.domain.dtos.HostRatingCreateRequest;
import ftn.ratingservice.domain.dtos.HostRatingDto;
import ftn.ratingservice.domain.dtos.HostRatingUpdateRequest;
import ftn.ratingservice.domain.entities.HostRating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HostRatingMapper {

    HostRatingMapper INSTANCE = Mappers.getMapper(HostRatingMapper.class);

    HostRatingDto toDto(HostRating hostRating);

    HostRating fromCreateRequest(HostRatingCreateRequest request);

    void update(@MappingTarget HostRating hostRating, HostRatingUpdateRequest request);

}
