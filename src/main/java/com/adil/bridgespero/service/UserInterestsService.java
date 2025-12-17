package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserInterestEntity;
import com.adil.bridgespero.domain.repository.CategoryRepository;
import com.adil.bridgespero.domain.repository.UserInterestRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInterestsService {

    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void replaceInterests(Long userId, List<Long> categoryIds) {

        deleteInterests(userId);

        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        createInterests(userId, categoryIds);
    }

    @Transactional
    protected void deleteInterests(Long userId) {
        userInterestRepository.deleteAllByUserId(userId);
        userInterestRepository.flush();
    }

    @Transactional
    protected void createInterests(Long userId, List<Long> categoryIds) {
        List<UserInterestEntity> interests =
                categoryIds.stream()
                        .distinct()
                        .map(categoryId -> buildInterest(userId, categoryId))
                        .toList();

        userInterestRepository.saveAll(interests);
    }

    private UserInterestEntity buildInterest(Long userId, Long categoryId) {
        return UserInterestEntity.builder()
                .user(userRepository.getReferenceById(userId))
                .category(categoryRepository.getReferenceById(categoryId))
                .build();
    }
}
