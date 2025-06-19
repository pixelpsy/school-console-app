package ua.foxminded.school.service;

import ua.foxminded.school.domain.Group;

import java.util.List;

public interface GroupService {
    List<Group> findGroupsWithLessOrEqualStudents(int maxStudents);
    void createGroup(String groupName);
    List<Group> findAllGroups();
}
