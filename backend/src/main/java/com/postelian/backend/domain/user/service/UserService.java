package com.postelian.backend.domain.user.service;

import com.postelian.backend.domain.user.dto.UserResponseDto;
import com.postelian.backend.domain.user.dto.UserSearchRequestDto;
import com.postelian.backend.domain.user.dto.UserSignUpRequestDto;
import com.postelian.backend.domain.user.dto.UserUpdateRequestDto;
import com.postelian.backend.domain.user.entity.User;
import com.postelian.backend.domain.user.entity.UserStatus;
import com.postelian.backend.domain.user.repository.UserRepository;
import com.postelian.backend.global.common.PageMetadata;
import com.postelian.backend.global.common.PageResponse;
import com.postelian.backend.global.error.ErrorCode;
import com.postelian.backend.global.error.exception.EntityNotFoundException;
import com.postelian.backend.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 다건 조회 (페이지네이션 및 필터링 검색)
     */
    public PageResponse<UserResponseDto> getUserList(UserSearchRequestDto dto) {
        Sort sort = (dto.getSort() != null) ? Sort.by(dto.getSort()) : Sort.unsorted();
        Pageable pageable = PageRequest.of(Math.max(0, dto.getPage() - 1), dto.getSize(), sort);

        Page<User> page = userRepository.search(dto.getName(), dto.getEmail(), dto.getRole(), pageable);
        return PageResponse.of(
                page.getContent().stream().map(UserResponseDto::from).collect(Collectors.toList()),
                PageMetadata.from(page)
        );
    }

    /**
     * 회원 가입
     */
    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto request, String createdBy) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(UserStatus.ACTIVE) // 가입 시 바로 활성화 상태로 지정
                .createdBy(createdBy)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponseDto.from(savedUser);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto request, String updatedBy) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(request.getName(), request.getPhoneNumber(), updatedBy);
        return UserResponseDto.from(user);
    }

    /**
     * 회원 탈퇴 (Soft Delete 및 WITHDRAWN 상태 변경)
     */
    @Transactional
    public void deleteUser(Long id, String deletedBy) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.updateStatus(UserStatus.WITHDRAWN, deletedBy);
        user.delete(deletedBy);
    }

    /**
     * 회원 단건 조회
     */
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.from(user);
    }
}
