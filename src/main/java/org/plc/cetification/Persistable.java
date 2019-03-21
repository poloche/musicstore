package org.plc.cetification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Persistable<T extends Persistable> {

    abstract void load(ResultSet resultSet, Connection connection) throws SQLException;

    abstract T getInstance();

    public void insert(String sql, Connection connection, Parameter... parameters) {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int pos = 1;
            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(Parameter.ParameterType.String)) {
                    statement.setString(pos, parameter.getStringValue());
                }
                if (parameter.getType().equals(Parameter.ParameterType.Int)) {
                    statement.setInt(pos, parameter.getIntValue());
                }
                pos++;
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T getById(String query, int id, Connection connection) {


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int pos = 1;
            statement.setInt(pos, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                T a = getInstance();
                a.load(rs, connection);
                return a;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String makeInQuery(String quey, String... parameters) {
        String questionMarks = Arrays.stream(parameters).map(param -> "?").collect(Collectors.joining(","));
        return quey + "(" + questionMarks + ")";
    }

    public static String getLikeTypeWord(String word, LikeType likeType) {
        String likeWord;
        switch (likeType) {
            case Start:
                likeWord = word + "%";
                break;
            case End:
                likeWord = "%" + word;
                break;
            default:
                likeWord = "%" + word + "%";
                break;
        }
        return likeWord;
    }
}
