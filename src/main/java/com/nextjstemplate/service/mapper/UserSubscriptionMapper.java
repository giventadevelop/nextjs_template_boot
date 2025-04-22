package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserSubscription;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.dto.UserSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSubscription} and its DTO {@link UserSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper extends EntityMapper<UserSubscriptionDTO, UserSubscription> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    UserSubscriptionDTO toDto(UserSubscription s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
