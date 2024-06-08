package ftn.ratingservice.domain.mappers;

import ftn.ratingservice.domain.dtos.LodgeRatingCreateRequest;
import ftn.ratingservice.domain.dtos.LodgeRatingDto;
import ftn.ratingservice.domain.dtos.LodgeRatingUpdateRequest;
import ftn.ratingservice.domain.entities.LodgeRating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LodgeRatingMapper {

    LodgeRatingMapper INSTANCE = Mappers.getMapper(LodgeRatingMapper.class);

    LodgeRatingDto toDto(LodgeRating lodgeRating);

    LodgeRating fromCreateRequest(LodgeRatingCreateRequest request);

    void update(@MappingTarget LodgeRating lodgeRating, LodgeRatingUpdateRequest request);
}
