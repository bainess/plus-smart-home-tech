    package ru.yandex.practicum.analyzer.model;

    import jakarta.persistence.*;
    import lombok.*;
    import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "actions")
    @Builder
    public class Action {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        private ActionTypeAvro type;

        private Integer value;

    }
