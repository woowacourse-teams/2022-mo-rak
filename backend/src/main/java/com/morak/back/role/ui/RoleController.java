package com.morak.back.role.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import com.morak.back.role.application.dto.RoleNameResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupCode}/roles")
public class RoleController {

    private final RoleService roleService;

    // -- A
    @GetMapping("/names")
    public RoleNameResponses findRoleNames(@PathVariable String groupCode,
                                           @Auth Long memberId) {
        return roleService.findRoleNames(groupCode, memberId);

    }


    // -- B

    @PutMapping("/names")
    public ResponseEntity<Void> doPoll(@PathVariable String groupCode,
                                       @Auth Long memberId,
                                       @Valid @RequestBody RoleNameEditRequest request) {
        roleService.editRoleNames(groupCode, memberId, request.getRoles());
        return ResponseEntity.ok().build();
    }

    // -- C



    // -- D


}
