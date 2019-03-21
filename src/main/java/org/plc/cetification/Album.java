package org.plc.cetification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Album extends Persistable<Album> {
    private int id;
    private String name;
    private int year;
    private Author author;
    private static final String insert = "INSERT INTO album(id, author, name, year) values(?,?,?,?)";
    private static final String byId = "SELECT * FROM album WHERE id=?";
    private static final String byYear = "SELECT * FROM album WHERE year=?";

    public static Album getNewAlbum(int id, String name, int year, Author author, Connection connection) {
        Album album = new Album();
        album.insert(
                insert, connection,
                new Parameter(Parameter.ParameterType.Int, id),
                new Parameter(Parameter.ParameterType.Int, author.getId()),
                new Parameter(Parameter.ParameterType.String, name),
                new Parameter(Parameter.ParameterType.Int, year)
        );
        album = getById(id, connection);
        return album;
    }

    @Override
    Album getInstance() {
        return new Album();
    }

    static Album getById(int id, Connection connection) {
        return new Album().getById(byId, id, connection);
    }

    public Album() {

    }

    @Override
    void load(ResultSet resultSet, Connection connection) throws SQLException {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
        year = resultSet.getInt("year");
        author = Author.getById(resultSet.getInt("author"), connection);
    }

    public int getId() {
        return id;
    }

    public static List<Album> getByAuthors(List<Author> authors, Connection connection) {
        String authorsQuestionMark = authors.stream().map(author1 -> "?").collect(Collectors.joining(","));
        String select = "SELECT * FROM album WHERE author IN (" + authorsQuestionMark + ")";
        List<Album> albums = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            int pos = 1;
            for (Author author : authors) {
                statement.setInt(pos, author.getId());
                pos++;
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Album a = new Album();
                a.load(rs, connection);
                albums.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    @Override
    public String toString() {
        return String.format("%5d %-30s %-20s ",
                year, name, author.getName()

        );
    }

    public String getName() {
        return name;
    }

    public Author getAuthor() {
        return author;
    }

    public static List<Album> getByYear(int year, Connection connection) {
        List<Album> albums = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(byYear)) {
            int pos = 1;
            statement.setInt(pos, year);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Album a = new Album();
                a.load(rs, connection);
                albums.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }
}
