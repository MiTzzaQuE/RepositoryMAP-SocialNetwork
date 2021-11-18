package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserFriendDTO {

    private final String firtsName;
    private final String lastName;
    private final LocalDateTime friendshipDate;

    public UserFriendDTO(String firtsName, String lastName, LocalDateTime friendshipDate) {
        this.firtsName = firtsName;
        this.lastName = lastName;
        this.friendshipDate = friendshipDate;
    }

    @Override
    public String toString() {
        return firtsName + "|" + lastName + "|" + friendshipDate;
    }
}
