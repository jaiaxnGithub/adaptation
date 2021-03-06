package com.jaiaxn.adaptation.controller;

import com.jaiaxn.adaptation.dto.AdaptationRequest;
import com.jaiaxn.adaptation.utils.dto.ResultVO;
import com.jaiaxn.adaptation.service.AdaptationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description:
 **/
@RestController
@RequestMapping("/api/adaptation")
@Slf4j
@Api(value="数据库统一适配器入口", tags={"数据库统一适配器入口"})
public class AdaptationController {

    private final AdaptationService adaptationService;

    @Autowired
    public AdaptationController(AdaptationService adaptationService) {
        this.adaptationService = adaptationService;
    }

    @ApiOperation(value = "统一入口", notes = "统一入口")
    @ApiResponses({
            @ApiResponse(code=400,message="请求参数没填好"),
            @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public ResultVO<Map<String, Object>> adaptationExec(@RequestBody @ApiParam(value = "统一入口", required = true) @Valid AdaptationRequest adaptationRequest) {
        return adaptationService.adaptationExec(adaptationRequest);
    }

    @ApiOperation(value = "测试初始化Client入口", notes = "测试初始化Client入口")
    @ApiResponses({
            @ApiResponse(code=400,message="请求参数没填好"),
            @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/test/client", method = RequestMethod.GET)
    public ResultVO<String> adaptationExec(@RequestParam("something") @ApiParam(value = "具体操作", required = true) String something) {
        return ResultVO.success(adaptationService.doSomething(something));
    }
}
