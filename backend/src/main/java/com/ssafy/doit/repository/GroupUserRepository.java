package com.ssafy.doit.repository;

import com.ssafy.doit.model.Group;
import com.ssafy.doit.model.GroupUser;
import com.ssafy.doit.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    Optional<GroupUser> findByGroupAndUser(Group group, User user);
    GroupUser findTopByGroup(Group group);
    List<GroupUser> findByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "delete from group_has_user where group_pk = ?1",nativeQuery = true)
    void deleteByGroupPk(Long groupPk);
}
