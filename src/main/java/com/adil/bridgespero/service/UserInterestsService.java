package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserInterestEntity;
import com.adil.bridgespero.domain.repository.CategoryRepository;
import com.adil.bridgespero.domain.repository.UserInterestRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInterestsService {

    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void replaceInterests(Long userId, List<Long> categoryIds) {

        userInterestRepository.deleteAllByUserId(userId);

        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        Set<UserInterestEntity> interests =
                categoryIds.stream()
                        .distinct()
                        .map(categoryId -> UserInterestEntity.builder()
                                .user(userRepository.getReferenceById(userId))
                                .category(categoryRepository.getReferenceById(categoryId))
                                .build()
                        )
                        .collect(Collectors.toSet());

        userInterestRepository.saveAll(interests);
    }
}
