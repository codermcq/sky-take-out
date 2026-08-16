package com.starchef.controller.admin;

import com.starchef.result.Result;
import com.starchef.service.WorkSpaceService;
import com.starchef.vo.WorkSpaceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    @ApiOperation("获取今日工作台聚合数据")
    @GetMapping
    public Result<WorkSpaceVO> getWorkSpaceData() {
        log.info("查询工作台数据");
        WorkSpaceVO workSpaceVO = workSpaceService.getWorkSpaceData();
        return Result.success(workSpaceVO);
    }
}
