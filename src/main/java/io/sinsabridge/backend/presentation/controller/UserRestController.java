// UserRestController.java
package io.sinsabridge.backend.presentation.controller;

import io.sinsabridge.backend.application.service.UserService;
import io.sinsabridge.backend.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        EntityModel<User> userEntityModel = EntityModel.of(user);
        userEntityModel.add(linkTo(methodOn(UserRestController.class).getUser(id)).withSelfRel());
        userEntityModel.add(linkTo(methodOn(UserRestController.class).getAllUsers(null)).withRel("users"));
        return ResponseEntity.ok(userEntityModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> createUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        EntityModel<User> userEntityModel = EntityModel.of(user);
        userEntityModel.add(linkTo(methodOn(UserRestController.class).getUser(user.getId())).withSelfRel());

        // 생성된 리소스의 URI를 가져옵니다.
        URI location = linkTo(methodOn(UserRestController.class).getUser(user.getId())).toUri();

        // 201 (Created) 상태 코드와 함께 생성된 리소스의 URI를 반환합니다.
        return ResponseEntity.created(location).body(userEntityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User userUpdates) {
        User updatedUser = userService.updateUser(id, userUpdates);
        EntityModel<User> userEntityModel = EntityModel.of(updatedUser);
        userEntityModel.add(linkTo(methodOn(UserRestController.class).getUser(id)).withSelfRel());
        return ResponseEntity.ok(userEntityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userService.findAllUsers(pageable);
        List<EntityModel<User>> users = usersPage.stream()
                .map(user -> {
                    Link selfLink = linkTo(methodOn(UserRestController.class).getUser(user.getId())).withSelfRel();
                    return EntityModel.of(user, selfLink);
                })
                .collect(Collectors.toList());

        Link link = linkTo(methodOn(UserRestController.class).getAllUsers(pageable)).withSelfRel();

        CollectionModel<EntityModel<User>> userCollectionModel = CollectionModel.of(users, link);
        return ResponseEntity.ok(userCollectionModel);
    }


}
