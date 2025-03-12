package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.springframework.stereotype.Component;

@Component
public class CommitteeMapper {
    private final UserMapper userMapper;

    public CommitteeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommitteeDto toDto(Committee committee) {
        if (committee == null) {
            return null;
        }
        CommitteeDto dto = new CommitteeDto();
        dto.setUserDto(userMapper.toDto(committee)); // Convert User part
        dto.setCommitteeName(committee.getCommitteeName());
        return dto;
    }

    public Committee toEntity(CommitteeDto dto) {
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
