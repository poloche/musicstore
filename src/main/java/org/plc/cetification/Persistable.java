package org.plc.cetification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Persistable<T extends Persistable> {
    private Class<T> instance;

    public Persistable(Class<T> instance) {
        this.instance = instance;
    }

    abstract void load(ResultSet resultSet, Connection connection) throws SQLException;

    public void insert(String sql, Connection connection, Parameter... parameters) {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadById(String query, int id, Connection connection) {


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int pos = 1;
            statement.setInt(pos, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                load(rs, connection);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<T> query(String sql, Connection connection, Parameter... parameters) {
        ArrayList<T> response = new ArrayList<>();
        if (hasInParameters(parameters)) {
            sql = makeInQuery(sql, parameters);
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                T newInstance = instance.newInstance();
                newInstance.load(rs, connection);
                response.add(newInstance);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean hasInParameters(Parameter... parameters) {
        return Arrays.stream(parameters).anyMatch(parameter -> parameter.getType().equals(Parameter.ParameterType.IN));
    }

    private String makeInQuery(String quey, Parameter... parameters) {
        Parameter paramIn = Arrays.stream(parameters).filter(parameter -> parameter.getType().equals(Parameter.ParameterType.IN)).findFirst().get();
        StringBuilder questionMarks = new StringBuilder();
        int size = paramIn.getValues().getValues().size();
        int curentSize = 1;
        for (Object i : paramIn.getValues().getValues()) {

            questionMarks.append("?");
            if (curentSize<size) {
                questionMarks.append(",");
                curentSize++;
            }
        }


        return quey + "(" + questionMarks + ")";
    }

    private String getLikeTypeWord(String word, LikeType likeType) {
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

    private void setParameters(PreparedStatement statement, Parameter... parameters) throws SQLException {
        int pos = 1;
        for (Parameter parameter : parameters) {
            switch (parameter.getType()) {
                case Int:
                    statement.setInt(pos, parameter.getIntValue());
                    break;
                case String:
                    statement.setString(pos, parameter.getStringValue());
                    break;
                case Like:
                    statement.setString(pos, getLikeTypeWord(parameter.getStringValue(), parameter.getLikeType()));
                    break;
                case IN:
                    Values values = parameter.getValues();
                    int newPos = pos;
                    for (Object value : values.getValues()) {
                        if (values.getType().equals(Values.Type.INT)) {
                            statement.setInt(newPos, (Integer) value);
                            newPos++;
                        }

                        if (values.getType().equals(Values.Type.STRING)) {
                            statement.setString(newPos, (String) value);
                            newPos++;
                        }
                    }
                    pos = newPos;
                default:
                    break;

            }
            pos++;
        }
    }
}
