package com.spring.boot.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "Events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    @Id
    private String id;

    @Field
    @NotNull
    private String name;

    @Field
    private String description;

    @Field
    @NotNull
    @Indexed(name = "venue_index", direction = IndexDirection.ASCENDING)
    private String venue;

    @Field
    private String speaker;

    @Field(name="created_date")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD")
    private Date creationDate;
}
