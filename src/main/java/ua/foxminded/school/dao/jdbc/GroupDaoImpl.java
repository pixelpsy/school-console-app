package ua.foxminded.school.dao.jdbc;

import ua.foxminded.school.dao.GroupDao;
import ua.foxminded.school.domain.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {

    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";

    private final Connection connection;

    public GroupDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxStudents) {
        List<Group> groups = new ArrayList<>();
        String sql =
            "SELECT g.group_id, g.group_name " +
            "FROM groups g " +
            "LEFT JOIN students s ON g.group_id = s.group_id " +
            "GROUP BY g.group_id, g.group_name " +
            "HAVING COUNT(s.student_id) <= ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, maxStudents);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(new Group(rs.getInt(GROUP_ID), rs.getString(GROUP_NAME)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find groups", e);
        }

        return groups;
    }

    @Override
    public void createGroup(String groupName) {
        String sql = "INSERT INTO groups (group_name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, groupName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert group", e);
        }
    }

    @Override
    public List<Group> findAllGroups() {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT group_id, group_name FROM groups";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                groups.add(new Group(rs.getInt(GROUP_ID), rs.getString(GROUP_NAME)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all groups", e);
        }

        return groups;
    }
    
    @Override
    public boolean existsById(int groupId) {
        String sql = "SELECT 1 FROM groups WHERE group_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check group existence", e);
        }
    }

}
