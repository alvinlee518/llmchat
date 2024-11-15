package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.security.SecurityUtils;
import ai.llmchat.server.api.param.OauthUserParam;
import ai.llmchat.server.api.vo.OauthUserVO;
import ai.llmchat.server.api.vo.RouteRecordVO;
import ai.llmchat.server.api.vo.UserDetailsVO;
import ai.llmchat.server.service.OauthMenuService;
import ai.llmchat.server.service.OauthUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final OauthUserService oauthUserService;
    private final OauthMenuService oauthMenuService;

    public AccountController(OauthUserService oauthUserService, OauthMenuService oauthMenuService) {
        this.oauthUserService = oauthUserService;
        this.oauthMenuService = oauthMenuService;
    }

    @GetMapping("/user_detail")
    public Result<UserDetailsVO> userInfo() {
        Long userId = SecurityUtils.getId();
        OauthUserVO user = oauthUserService.selectById(userId);
        List<String> permissionList = oauthMenuService.selectPermissionList(userId);
        return Result.data(new UserDetailsVO(user, permissionList));
    }

    @GetMapping("/route_list")
    public Result<List<RouteRecordVO>> routeList() {
        Long userId = SecurityUtils.getId();
        List<RouteRecordVO> list = oauthMenuService.selectAuthorizedMenuList(userId);
        return Result.data(list);
    }

    @PostMapping("/change_info")
    public Result<?> changeInfo(@RequestBody OauthUserParam param) {
        Long userId = SecurityUtils.getId();
        param.setId(userId);
        oauthUserService.saveOrUpdate(param);
        return Result.success();
    }
}
