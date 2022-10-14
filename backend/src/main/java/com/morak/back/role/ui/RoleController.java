package com.morak.back.role.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.role.application.RoleService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupCode}/roles")
public class RoleController {

    private final RoleService roleService;

    // -- A



    // -- B



    // -- C
    @PostMapping
    public ResponseEntity<Void> create(@PathVariable String groupCode, @Auth Long memberId) {
        Long roleId = roleService.match(groupCode, memberId);
        return ResponseEntity.created(URI.create("/api/groups/" + groupCode + "/roles/" + roleId)).build();
    }

    // -- D


}
