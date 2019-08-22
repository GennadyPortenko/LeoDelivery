package com.cmdelivery.service;

import com.cmdelivery.dto.ContractorDto;
import com.cmdelivery.dto.SectionDto;
import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Section;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DtoService {
    private final ModelMapper modelMapper;

    public static String parsePhone(String phone) {
        return phone.replaceAll("[^\\d.]", "").substring(1);
    }

    public static String toMaskedPhone(String phone) {
        return "+7 (" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6, 10);
    }

    public SectionDto convertToDto(Section section) {
        SectionDto sectionDto = modelMapper.map(section, SectionDto.class);
        sectionDto.setId(section.getSectionId());
        return sectionDto;
    }

    public Section convertToSection(SectionDto sectionDto) {
        Section section = modelMapper.map(sectionDto, Section.class);
        return section;
    }

    public ContractorDto convertToDto(Contractor contractor) {
        ContractorDto contractorDto = modelMapper.map(contractor, ContractorDto.class);
        contractorDto.setId(contractor.getContractorId());
        return contractorDto;
    }

    public Contractor convertToContractor(ContractorDto contractorDto) {
        Contractor contractor = modelMapper.map(contractorDto, Contractor.class);
        return contractor;
    }

}
