package com.j2kb.codev21.domains.gisucategory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.gisucategory.dto.GisuCategoryDto;
import com.j2kb.codev21.domains.gisucategory.dto.mapper.GisuCategoryMapper;
import com.j2kb.codev21.domains.gisucategory.repository.GisuCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GisuCategoryService {

	private final GisuCategoryMapper gisuCategoryMapper;
	
	private final GisuCategoryRepository gisuCategoryRepository;
	
    public List<GisuCategoryDto.Res> getGisuCatregoryList() {
    	List<GisuCategory> result = gisuCategoryRepository.findAll();
        return result.stream()
        			.map(gisuCategoryMapper::gisuCategoryToRes)
        			.collect(Collectors.toList());
    }

    public GisuCategoryDto.Res getGisuCategory(@PathVariable("gisuId") final long gisuId) {
    	GisuCategory result = gisuCategoryRepository.findById(gisuId)
    			.orElseThrow();
    	return gisuCategoryMapper.gisuCategoryToRes(result);
    }

    @Transactional
    public GisuCategoryDto.Res insertGisuCategory(GisuCategory gisuCategoryParam) {
    	GisuCategory result = gisuCategoryRepository.save(gisuCategoryParam);
        return gisuCategoryMapper.gisuCategoryToRes(result);
    }

    @Transactional
    public GisuCategoryDto.Res updateGisuCategory(final long gisuId, GisuCategory gisuCategoryParam) {
    	GisuCategory gisuCategory = gisuCategoryRepository.findById(gisuId)
    				.orElseThrow();
    	gisuCategory.updateGisuCategory(gisuCategoryParam);
    	gisuCategoryRepository.flush();
        return gisuCategoryMapper.gisuCategoryToRes(gisuCategory);
    }

    @Transactional
    public boolean deleteGisuCategory(final long gisuId) {
    	gisuCategoryRepository.deleteById(gisuId);
        return true;
    }
}
