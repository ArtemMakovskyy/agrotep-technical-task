package technikal.task.fishmarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fish")
@Getter
@Setter
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private double price;

    private Date catchDate;

    private String imageFileNames;

    @Transient
    public java.util.List<String> getImageFileNamesList() {
        if (imageFileNames == null || imageFileNames.isEmpty()) {
            return java.util.List.of();
        }
        return java.util.Arrays.asList(imageFileNames.split("\\s*,\\s*"));
    }
}
