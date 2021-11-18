package domain;

import java.time.LocalDate;

public class UserFriendDTO {

    private final String firtsName;
    private final String lastName;
    private final LocalDate friendshipDate;

    public UserFriendDTO(String firtsName, String lastName, LocalDate friendshipDate) {
        this.firtsName = firtsName;
        this.lastName = lastName;
        this.friendshipDate = friendshipDate;
    }

    @Override
    public String toString() {
        return "UserFriendDTO{" +
                "firtsName='" + firtsName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friendshipDate=" + friendshipDate +
                '}';
    }
}
