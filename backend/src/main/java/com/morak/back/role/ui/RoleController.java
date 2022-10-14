package com.morak.back.role.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import com.morak.back.role.application.dto.RoleNameResponses;
import com.morak.back.role.application.dto.RolesResponse;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupCode}/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/names")
    public ResponseEntity<RoleNameResponses> findRoleNames(@PathVariable String groupCode,
                                           @Auth Long memberId) {
        RoleNameResponses roleNames = roleService.findRoleNames(groupCode, memberId);
        return ResponseEntity.ok(roleNames);

    }

    @PutMapping("/names")
    public ResponseEntity<Void> doPoll(@PathVariable String groupCode,
                                       @Auth Long memberId,
                                       @Valid @RequestBody RoleNameEditRequest request) {
        roleService.editRoleNames(groupCode, memberId, request.getRoles());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable String groupCode, @Auth Long memberId) {
        Long roleId = roleService.match(groupCode, memberId);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/roles/" + roleId)).build();
    }

    @GetMapping("/histories")
    public ResponseEntity<RolesResponse> getHistories(@PathVariable String groupCode, @Auth Long memberId) {
        RolesResponse histories = roleService.findHistories(groupCode, memberId);
        return ResponseEntity.ok(histories);
    }


}
