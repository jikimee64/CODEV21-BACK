package com.j2kb.codev21.domains.gisucategory.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.j2kb.codev21.domains.gisucategory.domain.GisuCategory;
import com.j2kb.codev21.domains.gisucategory.dto.GisuCategoryDto;
import com.j2kb.codev21.domains.gisucategory.dto.mapper.GisuCategoryMapper;
import com.j2kb.codev21.domains.gisucategory.service.GisuCategoryService;
import com.j2kb.codev21.domains.team.dto.TeamDto;
import com.j2kb.codev21.domains.team.dto.TeamDto.SelectTeamRes;
import com.j2kb.codev21.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GisuCategoryController{
	
	private final GisuCategoryMapper gisuCategoryMapper;
	
    private final GisuCategoryService gisuCategoryService;
    
    // TODO: 유저 접근 권한 검증
    @GetMapping("/gisucategories")
    public CommonResponse<List<GisuCategoryDto.Res>> getGisuCatregoryList() {
        return CommonResponse.<List<GisuCategoryDto.Res>>builder()
            .code("200")
            .message("ok")
            .data(gisuCategoryService.getGisuCatregoryList())
            .build();
    }

    // TODO: 유저 접근 권한 검증
    @GetMapping("/gisucategories/{gisuId}")
    public CommonResponse<GisuCategoryDto.Res> getGisuCategory(@PathVariable("gisuId") final long gisuId) {
    	
        return CommonResponse.<GisuCategoryDto.Res>builder()
            .code("200")
            .message("ok")
            .data(gisuCategoryService.getGisuCategory(gisuId))
            .build();
    }

    // TODO: 관리저 접근 권한 검증
    @PostMapping("/admin/gisucategories")
    public CommonResponse<GisuCategoryDto.Res> insertGisuCategory(
        @RequestBody final GisuCategoryDto.Req req) {
    	GisuCategory gisuCategoryParam = gisuCategoryMapper.reqToGisuCategory(req);
        return CommonResponse.<GisuCategoryDto.Res>builder()
            .code("200")
            .message("ok")
            .data(gisuCategoryService.insertGisuCategory(gisuCategoryParam))
            .build();
    }

    // TODO: 관리저 접근 권한 검증
    @PatchMapping(value = "/admin/gisucategories/{gisuId}")
    public CommonResponse<GisuCategoryDto.Res> updateGisuCategory(
        @PathVariable("gisuId") final long gisuId,
        @RequestBody final GisuCategoryDto.Req req) {
    	GisuCategory gisuCategoryParam = gisuCategoryMapper.reqToGisuCategory(req);
    	
        return CommonResponse.<GisuCategoryDto.Res>builder()
            .code("200")
            .message("ok")
            .data(gisuCategoryService.updateGisuCategory(gisuId, gisuCategoryParam))
            .build();
    }
    
    // TODO: 관리저 접근 권한 검증
    @DeleteMapping(value = {"/admin/gisucategories/{gisuId}"})
    public CommonResponse<HashMap<String, Boolean>> deleteGisuCategory(@PathVariable("gisuId") final long gisuId) {
		HashMap<String, Boolean> dataWrapper = new HashMap<String, Boolean>();
		Boolean result = gisuCategoryService.deleteGisuCategory(gisuId);
		dataWrapper.put("result", result);
        return CommonResponse.<HashMap<String, Boolean>>builder()
            .code("200")
            .message("ok")
            .data(dataWrapper)
            .build();
    }

}
