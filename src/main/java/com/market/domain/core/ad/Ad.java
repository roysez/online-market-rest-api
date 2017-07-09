package com.market.domain.core.ad;

import com.market.domain.core.user.User;
import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Ad implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column(nullable = false)
    private BigDecimal rate;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    private Location location;
    private String comment;
    @Lob
    private LocalDateTime publishedAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public Ad publish() {
        if (status == Status.NEW) {
            publishedAt = LocalDateTime.now();
            status = Status.PUBLISHED;
        } else {
            throw new InvalidAdStateTransitionException("Ad is already published");
        }
        return this;
    }

    public Ad expire() {
        if (status == Status.PUBLISHED) {
            status = Status.EXPIRED;
        } else {
            throw new InvalidAdStateTransitionException(
                    "Ad can be expire only when it is in the " + Status.PUBLISHED + " state");
        }
        return this;
    }

    public enum Type {

        BUY,

        SELL

    }

    public enum Currency {

        USD,

        EUR

    }

    public enum Status {

        NEW,

        PUBLISHED,

        EXPIRED

    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {

        @Column(nullable = false)
        private String city;

        private String area;

    }
}
