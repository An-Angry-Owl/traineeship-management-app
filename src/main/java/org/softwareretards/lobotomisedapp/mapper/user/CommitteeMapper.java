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
        dto.setId(committee.getId());
        dto.setUsername(committee.getUsername());
        dto.setPassword(committee.getPassword());
        dto.setRole(committee.getRole());
        dto.setEnabled(committee.isEnabled());
        dto.setCreatedAt(committee.getCreatedAt());
        dto.setUpdatedAt(committee.getUpdatedAt());
        dto.setCommitteeName(committee.getCommitteeName());
        return dto;
    }

    public static Committee toEntity(CommitteeDto dto) {
        if (dto == null) {
            return null;
        }
        Committee committee = new Committee();
        committee.setId(dto.getId());
        committee.setUsername(dto.getUsername());
        committee.setPassword(dto.getPassword());
        committee.setRole(dto.getRole());
        committee.setEnabled(dto.isEnabled());
        committee.setCreatedAt(dto.getCreatedAt());
        committee.setUpdatedAt(dto.getUpdatedAt());
        committee.setCommitteeName(dto.getCommitteeName());
        return committee;
    }
}
