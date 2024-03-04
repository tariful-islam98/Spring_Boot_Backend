package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.Privilege;
import com.practice.springboot.payloads.PrivilegeDto;
import com.practice.springboot.repositories.PrivilegeRepository;
import com.practice.springboot.services.interfaces.PrivilegeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, ModelMapper modelMapper) {
        this.privilegeRepository = privilegeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PrivilegeDto createPrivilege(PrivilegeDto privilegeDto) {
        // Map DTO to Entity
        Privilege privilege = modelMapper.map(privilegeDto, Privilege.class);

        // Save the privilege
        Privilege savedPrivilege = privilegeRepository.save(privilege);

        // Map Entity back to DTO and return
        return modelMapper.map(savedPrivilege, PrivilegeDto.class);
    }

    @Override
    public PrivilegeDto updatePrivilege(PrivilegeDto privilegeDto, Long privilegeId) {
        // Find the privilege by ID
        Privilege privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilege not found with id: " + privilegeId));

        // Update privilege fields
        privilege.setName(privilegeDto.getName()); // Assuming only name can be updated

        // Save the updated privilege
        Privilege updatedPrivilege = privilegeRepository.save(privilege);

        // Map Entity back to DTO and return
        return modelMapper.map(updatedPrivilege, PrivilegeDto.class);
    }

    @Override
    public PrivilegeDto getPrivilegeById(Long privilegeId) {
        // Find the privilege by ID
        Privilege privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilege not found with id: " + privilegeId));

        // Map Entity to DTO and return
        return modelMapper.map(privilege, PrivilegeDto.class);
    }

    @Override
    public List<PrivilegeDto> getAllPrivileges() {
        // Get all privileges from the repository
        List<Privilege> privileges = privilegeRepository.findAll();

        // Map Entity list to DTO list and return
        return privileges.stream()
                .map(privilege -> modelMapper.map(privilege, PrivilegeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePrivilege(Long privilegeId) {
        // Check if privilege exists
        if (!privilegeRepository.existsById(privilegeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Privilege not found with id: " + privilegeId);
        }

        // Delete privilege by ID
        privilegeRepository.deleteById(privilegeId);
    }
}
