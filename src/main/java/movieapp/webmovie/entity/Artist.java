package movieapp.webmovie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "artists")
@Getter
@Setter
public class Artist {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistID;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String bio;

}
