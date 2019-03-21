package org.plc.cetification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cancion extends Persistable<Cancion> {
    private int id;
    private int position;
    private String name;
    private Album album;
    private Connection connection;
    private static final String insert = "INSERT INTO cancion(id, album, name, number) values(?,?,?,?)";
    private static final String byId = "SELECT * FROM cancion WHERE id=?";
    private static final String byWordInName = "SELECT * FROM cancion WHERE name LIKE ?";
    private static final String byAlbumName = "SELECT c.id as id,c.name as name, c.album as album, c.number as number FROM cancion c JOIN album a ON a.id = c.album WHERE a.name IN ";

    public static Cancion insert(int id, int position, String name, Album album, Connection connection) {
        Cancion cancion = new Cancion();
        cancion.insert(insert, connection,
                new Parameter(Parameter.ParameterType.Int, id),
                new Parameter(Parameter.ParameterType.Int, album.getId()),
                new Parameter(Parameter.ParameterType.String, name),
                new Parameter(Parameter.ParameterType.Int, position)
        );
        return cancion.getById(byId, id, connection);
    }

    public Cancion() {

    }

    @Override
    void load(ResultSet resultSet, Connection connection) throws SQLException {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
        position = resultSet.getInt("number");
        album = Album.getById(resultSet.getInt("album"), connection);
    }

    @Override
    Cancion getInstance() {
        return new Cancion();
    }

    public static List<Cancion> getByAlbumns(List<Album> albums, Connection connection) {
        String albumsQuestionMark = albums.stream().map(author1 -> "?").collect(Collectors.joining(","));
        String select = "SELECT * FROM cancion WHERE album IN (" + albumsQuestionMark + ")";
        List<Cancion> songs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            int pos = 1;
            for (Album album : albums) {
                statement.setInt(pos, album.getId());
                pos++;
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Cancion a = new Cancion();
                a.load(rs, connection);
                songs.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public String toString() {
        return String.format(" %3d   %-50s  %-30s %-20s", position, name, album.getName(), album.getAuthor().getName());
    }

    public static List<Cancion> getByName(String wordInName, LikeType likeType, Connection connection) {
        List<Cancion> cancions = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(byWordInName)) {
            int pos = 1;
            statement.setString(pos, getLikeTypeWord(wordInName, likeType));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Cancion a = new Cancion();
                a.load(rs, connection);
                cancions.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cancions;
    }

    public static List<Cancion> getByAlbumName(Connection connection, String... albumNames) {
        List<Cancion> cancions = new ArrayList<>();
        String query = makeInQuery(byAlbumName, albumNames);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int pos = 1;
            for (String albumName : albumNames) {
                statement.setString(pos, albumName);
                pos++;
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Cancion a = new Cancion();
                a.load(rs, connection);
                cancions.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cancions;
    }
}
