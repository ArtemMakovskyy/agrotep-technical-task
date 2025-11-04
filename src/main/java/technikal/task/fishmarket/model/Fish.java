package technikal.task.fishmarket.model;

import jakarta.persistence.Column;
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
    private Long id;

    private String name;

    private double price;

    @Column(name = "catch_date")
    private Date catchDate;

    @Column(name = "image_file_names")
    private String imageFileNames;

    @Transient
    public java.util.List<String> getImageFileNamesList() {
        if (imageFileNames == null || imageFileNames.isEmpty()) {
            return java.util.List.of();
        }
        return java.util.Arrays.asList(imageFileNames.split("\\s*,\\s*"));
    }
}
