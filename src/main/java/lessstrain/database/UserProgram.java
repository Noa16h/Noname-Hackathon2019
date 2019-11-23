package lessstrain.database;

import java.util.Objects;

public final class UserProgram {
    private final int id;
    private String displayName;
    private String path;

    UserProgram(final int id, final String displayName, final String path) {
        this.id = id;
        this.displayName = displayName;
        this.path = path;
    }

    public UserProgram(final String displayName, final String path) {
        this(0, displayName, path);
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProgram)) return false;
        final UserProgram that = (UserProgram) o;
        return id == that.id &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, path);
    }
}
