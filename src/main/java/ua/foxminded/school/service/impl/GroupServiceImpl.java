package ua.foxminded.school.service.impl;

import ua.foxminded.school.dao.GroupDao;
import ua.foxminded.school.domain.Group;
import ua.foxminded.school.service.GroupService;

import java.util.List;
import java.util.Objects;

public class GroupServiceImpl implements GroupService {

    private final GroupDao groupDao;

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = Objects.requireNonNull(groupDao);
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        return groupDao.findGroupsWithLessOrEqualStudents(maxStudents);
    }

    @Override
    public void createGroup(String groupName) {
        groupDao.createGroup(groupName);
    }

    @Override
    public List<Group> findAllGroups() {
        return groupDao.findAllGroups();
    }
}
