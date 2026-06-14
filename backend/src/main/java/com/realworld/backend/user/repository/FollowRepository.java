package com.realworld.backend.user.repository;

import com.realworld.backend.user.entity.Follow;
import com.realworld.backend.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  @Query("select f from Follow f join fetch f.from join f.to where f.to.username = :toName and f.from.username = :fromName")
  Optional<Follow> findByFromUserNameAndToUserName(@Param("fromName") String fromName,
      @Param("toName") String toName);

  @Query("delete from Follow f where from.username = :fromName and to.username = :toName")
  void deleteByFromUserNameAndToUserName(@Param("fromName") String fromName,
      @Param("toName") String toName);

  @Query("select f.to from Follow f where f.from.username = :fromName")
  List<User> findByFromUserNameInUser(@Param("fromName") String fromName);

  @Query("select f.to.username from Follow f where f.from.username = :fromName")
  List<String> findByFromUserNameInString(@Param("fromName") String fromName);
}
