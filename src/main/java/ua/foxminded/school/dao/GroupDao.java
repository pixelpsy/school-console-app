package ua.foxminded.school.dao;

import ua.foxminded.school.domain.Group;

import java.util.List;

public interface GroupDao {
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    void createGroup(String groupName);
    List<Group> findAllGroups();
    boolean existsById(int groupId);
}
