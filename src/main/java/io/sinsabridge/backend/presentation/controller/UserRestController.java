// UserRestController.java
package io.sinsabridge.backend.presentation.controller;

import io.sinsabridge.backend.application.service.UserService;
import io.sinsabridge.backend.domain.entity.User;
import io.sinsabridge.backend.presentation.dto.UserDto;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.service.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final SmsSender smsSender;

    /**
     * 모든 사용자를 조회하는 메서드야. 페이지 정보가 주어지면, 해당 페이지에 있는 사용자를 보여줄게!
     * 뭐 어렵지 않아, 그냥 사용자 목록을 가져오는 거야. 페이지 별로 나눠서 보여주는 거지!
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers(Pageable pageable) {
        Page<User> users = userService.findAllUsers(pageable);
        List<EntityModel<User>> userEntities = users.stream()
                .map(user -> EntityModel.of(user, createUserLinks(user)))
                .collect(Collectors.toList());
        Link selfLink = linkTo(methodOn(UserRestController.class).getUsers(pageable)).withSelfRel();
        CollectionModel<EntityModel<User>> userCollectionModel = CollectionModel.of(userEntities, selfLink);
        return ResponseEntity.ok(userCollectionModel);
    }

    /**
     * 사용자 상세 정보를 조회하는 메서드야. 사용자 ID를 입력받으면, 해당 사용자 정보를 보여줄게!
     * 이 메서드는 사용자 상세 정보를 보여주는 거야. 그냥 ID를 주면 찾아서 보여주는 거고!
     */
    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        return userOptional.map(user -> {
            EntityModel<User> userEntityModel = EntityModel.of(user, createUserLinks(user));
            return ResponseEntity.ok(userEntityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 문자 메시지 발송 요청 처리
    @PostMapping("/send-sms")
    public ResponseEntity<Void> sendSms(@RequestParam String phoneNumber) {
        smsSender.send(new SmsSendRequest(phoneNumber));
        return ResponseEntity.ok().build();
    }

    // 문자 메시지 인증 코드 확인 요청 처리
    @PostMapping("/verify-sms")
    public ResponseEntity<Boolean> verifySms(@RequestParam String phoneNumber,
                                             @RequestParam String code) {
        boolean verified = userService.verifySmsCode(phoneNumber, code);
        return ResponseEntity.ok(verified);
    }

    // 회원 가입 요청 처리
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto request) {
        UserDto response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }


    /**
     * 사용자 정보를 수정하는 메서드야. 사용자 ID와 수정할 정보를 입력받으면, 해당 사용자 정보를 수정해줄게!
     * 이 메서드는 사용자 정보를 업데이트하는 거야. ID를 주면 찾아서 정보를 수정하는 거고!
     */
    @PutMapping("/{userId}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable Long userId,
                                                        @RequestBody @Valid UserDto userDto) {
        User updatedUser = userService.updateUser(userId, userDto);
        EntityModel<User> userEntityModel = EntityModel.of(updatedUser, createUserLinks(updatedUser));
        return ResponseEntity.ok(userEntityModel);
    }

    /**
     * 사용자를 삭제하는 메서드야. 사용자 ID를 입력받으면, 해당 사용자를 삭제해줄게!
     * 이 메서드는 사용자를 삭제하는 거야. ID를 주면 찾아서 삭제하는 거고!
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 사용자에 대한 링크를 생성하는 메서드야. 사용자 정보를 입력받으면, 링크를 생성해서 반환해줄게!
     * 이 메서드는 사용자 정보에 대한 링크를 만드는 거야. 그냥 사용자 정보를 주면 링크를 생성해서 반환하는 거고!
     */
    private Link[] createUserLinks(User user) {
        Link selfLink = linkTo(methodOn(UserRestController.class).getUser(user.getId())).withSelfRel();
        Link usersLink = linkTo(methodOn(UserRestController.class).getUsers(Pageable.unpaged())).withRel("users");
        return new Link[]{selfLink, usersLink};
    }
}