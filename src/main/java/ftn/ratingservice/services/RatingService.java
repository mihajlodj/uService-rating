package ftn.ratingservice.services;

import ftn.ratingservice.domain.dtos.*;
import ftn.ratingservice.domain.entities.*;
import ftn.ratingservice.domain.mappers.HostRatingMapper;
import ftn.ratingservice.domain.mappers.LodgeRatingMapper;
import ftn.ratingservice.exception.exceptions.BadRequestException;
import ftn.ratingservice.exception.exceptions.ForbiddenException;
import ftn.ratingservice.exception.exceptions.NotFoundException;
import ftn.ratingservice.repositories.HostRatingRepository;
import ftn.ratingservice.repositories.LodgeRatingRepository;
import ftn.ratingservice.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final HostRatingRepository hostRatingRepository;
    private final LodgeRatingRepository lodgeRatingRepository;
    private final NotificationService notificationService;
    private final RestService restService;

    public List<HostRatingDto> getUserHostRatings(String hostId) {
        List<HostRating> ratings;
        if (hostId != null) {
            ratings = hostRatingRepository.getAllByCreatedBy_UserIdAndHostId(AuthUtils.getLoggedUserId().toString(), hostId);
        } else {
            ratings = hostRatingRepository.getAllByCreatedBy_UserId(AuthUtils.getLoggedUserId().toString());
        }

        return ratings.stream().map(HostRatingMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public List<HostRatingDto> getHostRatings(String hostId) {
        List<HostRating> ratings = hostRatingRepository.getAllByHostId(hostId);
        return ratings.stream().map(HostRatingMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public HostRatingDto getHostRating(String id) {
        HostRating rating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        return HostRatingMapper.INSTANCE.toDto(rating);
    }

    public Double getAverageHostRating(String hostId) {
        List<HostRating> ratings = hostRatingRepository.getAllByHostId(hostId);
        if (ratings.isEmpty()) {
            return null;
        }

        double total = 0;
        for (HostRating rating : ratings) {
            total += rating.getRating();
        }
        return total / ratings.size();
    }

    public HostRatingDto createHostRating(HostRatingCreateRequest createRequest) {
        UserDto userDto = restService.getUserById(UUID.fromString(createRequest.getHostId()));
        if (userDto.getRole() != UserRole.HOST) {
            throw new BadRequestException("Can't rate non host");
        }

        HostRating hostRating = HostRatingMapper.INSTANCE.fromCreateRequest(createRequest);
        User createdBy = User.builder()
                .userId(AuthUtils.getLoggedUserId().toString())
                .username(AuthUtils.getLoggedUsername())
                .build();
        hostRating.setCreatedBy(createdBy);

        HostRatingDto createdRating = HostRatingMapper.INSTANCE.toDto(hostRatingRepository.save(hostRating));
        notificationService.sendNotification(hostRating.getHostId(), NotificationType.HOST_RATING);
        return createdRating;
    }

    public HostRatingDto updateHostRating(String id, HostRatingUpdateRequest updateRequest) {
        HostRating hostRating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!hostRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't edit other's rating");
        }

        HostRatingMapper.INSTANCE.update(hostRating, updateRequest);
        return HostRatingMapper.INSTANCE.toDto(hostRatingRepository.save(hostRating));
    }

    public void deleteHostRating(String id) {
        HostRating hostRating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!hostRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't delete other's rating");
        }

        hostRatingRepository.deleteById(id);
    }

    public List<LodgeRatingDto> getLodgeRatings(String lodgeId) {
        List<LodgeRating> ratings = lodgeRatingRepository.getAllByLodgeId(lodgeId);
        return ratings.stream().map(LodgeRatingMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public List<LodgeRatingDto> getUserLodgeRatings(String lodgeId) {
        List<LodgeRating> ratings;
        if (lodgeId != null) {
            ratings = lodgeRatingRepository.getAllByCreatedBy_UserIdAndLodgeId(AuthUtils.getLoggedUserId().toString(), lodgeId);
        } else {
            ratings = lodgeRatingRepository.getAllByCreatedBy_UserId(AuthUtils.getLoggedUserId().toString());
        }

        return ratings.stream().map(LodgeRatingMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public LodgeRatingDto getLodgeRating(String id) {
        LodgeRating rating = lodgeRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        return LodgeRatingMapper.INSTANCE.toDto(rating);
    }

    public Double getAverageLodgeRating(String lodgeId) {
        List<LodgeRating> ratings = lodgeRatingRepository.getAllByLodgeId(lodgeId);
        if (ratings.isEmpty()) {
            return null;
        }

        double total = 0;
        for (LodgeRating rating : ratings) {
            total += rating.getRating();
        }
        return total / ratings.size();
    }

    public LodgeRatingDto createLodgeRating(LodgeRatingCreateRequest createRequest) {
        LodgeRating lodgeRating = LodgeRatingMapper.INSTANCE.fromCreateRequest(createRequest);
        User createdBy = User.builder()
                .userId(AuthUtils.getLoggedUserId().toString())
                .username(AuthUtils.getLoggedUsername())
                .build();
        lodgeRating.setCreatedBy(createdBy);

        LodgeRatingDto createdRating = LodgeRatingMapper.INSTANCE.toDto(lodgeRatingRepository.save(lodgeRating));
        notificationService.sendNotification(lodgeRating.getHostId(), NotificationType.HOTEL_RATING);
        return createdRating;
    }

    public LodgeRatingDto updateLodgeRating(String id, LodgeRatingUpdateRequest updateRequest) {
        LodgeRating lodgeRating = lodgeRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!lodgeRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't edit other's rating");
        }

        LodgeRatingMapper.INSTANCE.update(lodgeRating, updateRequest);
        return LodgeRatingMapper.INSTANCE.toDto(lodgeRatingRepository.save(lodgeRating));
    }

    public void deleteLodgeRating(String id) {
        LodgeRating lodgeRating = lodgeRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!lodgeRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't delete other's rating");
        }

        lodgeRatingRepository.deleteById(id);
    }

}
