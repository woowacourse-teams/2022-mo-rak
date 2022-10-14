package com.morak.back.role.ui;

import com.morak.back.auth.support.Auth;
import com.morak.back.poll.ui.dto.PollResultRequest;
import com.morak.back.role.application.RoleService;
import com.morak.back.role.application.dto.RoleNameEditRequest;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // -- A



    // -- B

    @PutMapping("/api/groups/{groupCode}/roles/names")
    public ResponseEntity<Void> doPoll(@PathVariable String groupCode,
                                       @Auth Long memberId,
                                       @Valid @RequestBody RoleNameEditRequest request) {
        roleService.editRoleNames(groupCode, memberId, request.getRoles());
        return ResponseEntity.ok().build();
    }

    // -- C



    // -- D


}
