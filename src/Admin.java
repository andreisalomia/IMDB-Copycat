public class Admin<T extends Comparable<T>> extends Staff<T> {
    public Admin(Information info, AccountType type, String username) {
        super(info, type, username);
    }

    public void addUser(User<?> user) {
        // add user using IMDB class
    }

    public void removeUser(User<?> user) {
        // remove user using IMDB class
        removeAssosiatedData(user);
    }

    private void removeAssosiatedData(User<?> user) {
        // remove all data associated with user
    }

}
