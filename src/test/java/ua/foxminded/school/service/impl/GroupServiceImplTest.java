package ua.foxminded.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.school.dao.GroupDao;
import ua.foxminded.school.domain.Group;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {

    private GroupDao groupDao;
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        groupDao = mock(GroupDao.class);
        groupService = new GroupServiceImpl(groupDao);
    }

    @Test
    void findGroupsWithLessOrEqualStudents_shouldReturnMatchingGroups_whenGroupsExist() {
        List<Group> expected = Arrays.asList(
            new Group(1, "GR-01"),
            new Group(2, "GR-02")
        );

        when(groupDao.findGroupsWithLessOrEqualStudents(10)).thenReturn(expected);

        List<Group> actual = groupService.findGroupsWithLessOrEqualStudents(10);

        assertEquals(2, actual.size());
        assertEquals("GR-01", actual.get(0).getGroupName());
        verify(groupDao).findGroupsWithLessOrEqualStudents(10);
    }

    @Test
    void findGroupsWithLessOrEqualStudents_shouldReturnEmptyList_whenNoGroupsFound() {
        when(groupDao.findGroupsWithLessOrEqualStudents(0)).thenReturn(Collections.emptyList());

        List<Group> result = groupService.findGroupsWithLessOrEqualStudents(0);

        assertTrue(result.isEmpty());
        verify(groupDao).findGroupsWithLessOrEqualStudents(0);
    }
}
