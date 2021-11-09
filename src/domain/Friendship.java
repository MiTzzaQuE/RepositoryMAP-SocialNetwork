package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Friendship class which extends an Entity
 */
public class Friendship extends Entity<Tuple<Long,Long>> {

    /**
     * LocalDateTime
     */
    private LocalDateTime date;

    /**
     * default constructor for object Friendship
     */
    public Friendship(){}

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * set the date time when the friendship was created
     * @param Date-LocalDateTime
     */
    public void setDate(LocalDateTime Date){date=Date;}
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Friendship{" +getId().getLeft()+" "+getId().getRight()+" "+
                "date=" + date.format(formatter) +
                '}';
    }

}
