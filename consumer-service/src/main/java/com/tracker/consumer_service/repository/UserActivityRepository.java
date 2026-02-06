package com.tracker.consumer_service.repository;

import com.tracker.consumer_service.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}
