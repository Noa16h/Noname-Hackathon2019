package lessstrain.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class Database {
    public static void main(String[] args) throws SQLException, InterruptedException {
        final Database database = new Database();
        database.addFrame(new Frame(20, 100));
        sleep(10);
        database.addFrame(new Frame(10, 300));
        sleep(10);
        database.addFrame(new Frame(0, 200));
        database.addUserProgram(new UserProgram("display name1", "path1"));
        database.addUserProgram(new UserProgram("display name2", "path2"));
        database.addUserProgram(new UserProgram("display name3", "path3"));
        System.out.println(Arrays.toString(database.listFrames().toArray()));
        System.out.println(Arrays.toString(database.listUserPrograms().toArray()));
    }

    private final Connection connection;

    public Database() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException e) {
            System.err.println("Can't find the database driver");
            e.printStackTrace();
        }
        System.out.println("Connecting to the database");
        connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        System.out.println("Connection established");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    if (connection.isClosed())
                        System.out.println("Connection to Database closed");
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * Inserts a frame into the database, if the frame timestamp isn't zero, it will update instead.
     *
     * @param frame Frame to insert
     */
    public void addFrame(final Frame frame) {
        checkTableStructure();
        try (final Statement statement = connection.createStatement()) {
            if (frame.getTimestamp() == 0)
                statement.execute(String.format("INSERT INTO frames(distance, apm) values(%.3f, %d);",
                        frame.getDistance(), frame.getApm()));
            else
                statement.execute(String.format("UPDATE frames SET distance=%.3f, apm=%d WHERE ftimestamp=%d;",
                        frame.getDistance(), frame.getApm(), frame.getTimestamp()));
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return A List with all frames from the database
     */
    public List<Frame> listFrames() {
        checkTableStructure();
        final List<Frame> frames = new ArrayList<>(30);
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT ftimestamp, distance, apm from frames;");
            while (resultSet.next()) {
                frames.add(new Frame(resultSet.getTime("ftimestamp").getTime(), resultSet.getDouble("distance"),
                        resultSet.getInt("apm")));
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return frames;
    }

    /**
     * Inserts a userprogram into the database, if the program id isn't zero, it will update instead.
     *
     * @param program Userprogram to insert
     */
    public void addUserProgram(final UserProgram program) {
        checkTableStructure();
        try (final Statement statement = connection.createStatement()) {
            if (program.getId() == 0)
                statement.execute(String.format("INSERT INTO programs(display_name, path) values(%s, %s);",
                        program.getDisplayName(), program.getPath()));
            else
                statement.execute(String.format("UPDATE programs SET display_name='%s', path='%s' WHERE id=%d;",
                        program.getDisplayName(), program.getPath(), program.getId()));
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a userprogram with the corresponding id
     * @param id The id of the userprogram to be deleted
     */
    public void removeUserProgram(final int id) {
        checkTableStructure();
        try (final Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM programs WHERE id=" + id + ";");
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return A List with all userprograms from the database
     */
    public List<UserProgram> listUserPrograms() {
        checkTableStructure();
        final List<UserProgram> programs = new ArrayList<>(30);
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT id, display_name, path from programs;");
            while (resultSet.next()) {
                programs.add(new UserProgram(resultSet.getInt("id"), resultSet.getString("display_name"),
                        resultSet.getString("path")));
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return programs;
    }

    // TODO: Add processes (relation between frames and programs)

    /**
     * Checks if the tables frames, programes and processes exists, and if not, creates them with the right structure.
     */
    private void checkTableStructure() {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS frames " +
                    "(ftimestamp timestamp PRIMARY KEY DEFAULT CURRENT_TIMESTAMP, distance double NOT NULL, apm default 0);");
            statement.execute("CREATE TABLE IF NOT EXISTS programs " +
                    "(id integer PRIMARY KEY, display_name varchar(255) NOT NULL, path varchar(500) NOT NULL);");
            statement.execute("CREATE TABLE IF NOT EXISTS processes " +
                    "(ftimestamp timestamp NOT NULL, program_id integer NOT NULL, " +
                    "FOREIGN KEY(ftimestamp) REFERENCES frames(ftimestamp), " +
                    "FOREIGN KEY(program_id) REFERENCES programs(id));");
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}
