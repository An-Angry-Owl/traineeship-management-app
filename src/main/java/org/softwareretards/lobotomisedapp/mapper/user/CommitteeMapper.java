package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.springframework.stereotype.Component;

@Component
public class CommitteeMapper {

    public static CommitteeDto toDto(Committee committee) {
        if (committee == null) {
            return null;
        }
        CommitteeDto dto = new CommitteeDto();
        dto.setUserDto(UserMapper.toDto(committee)); // Convert User part
        dto.setCommitteeName(committee.getCommitteeName());
        return dto;
    }

    public static Committee toEntity(CommitteeDto dto) {
        if (dto == null) {
            return null;
        }
        Committee committee = new Committee();
        committee.setId(dto.getUserDto().getId());  // ID from UserDto
        committee.setUsername(dto.getUserDto().getUsername());
        committee.setRole(dto.getUserDto().getRole());
        committee.setEnabled(dto.getUserDto().isEnabled());
        committee.setCreatedAt(dto.getUserDto().getCreatedAt());
        committee.setUpdatedAt(dto.getUserDto().getUpdatedAt());
        committee.setCommitteeName(dto.getCommitteeName());
        return committee;
    }
}