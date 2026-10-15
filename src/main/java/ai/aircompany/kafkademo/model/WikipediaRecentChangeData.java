package ai.aircompany.kafkademo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "wikipedia_recentchange")
public class WikipediaRecentChangeData {

    @JsonProperty("$schema")
    @Field(type = FieldType.Keyword)
    private String schema;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @JsonProperty("id")
    @Field(type = FieldType.Long)
    private Long changeId;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Integer)
    private Integer namespace;

    @Field(type = FieldType.Text)
    private String title;

    @JsonProperty("title_url")
    @Field(type = FieldType.Keyword)
    private String titleUrl;

    @Field(type = FieldType.Text)
    private String comment;

    @Field(type = FieldType.Text)
    private String parsedcomment;

    @Field(type = FieldType.Long)
    private Long timestamp;

    @Field(type = FieldType.Text)
    private String user;

    @Field(type = FieldType.Boolean)
    private Boolean bot;

    @JsonProperty("notify_url")
    @Field(type = FieldType.Keyword)
    private String notifyUrl;

    @Field(type = FieldType.Boolean)
    private Boolean minor;

    @Field(type = FieldType.Boolean)
    private Boolean patrolled;

    @Field(type = FieldType.Object)
    private Length length;

    @Field(type = FieldType.Object)
    private Revision revision;

    @JsonProperty("server_url")
    @Field(type = FieldType.Keyword)
    private String serverUrl;

    @JsonProperty("server_name")
    @Field(type = FieldType.Keyword)
    private String serverName;

    @JsonProperty("server_script_path")
    @Field(type = FieldType.Keyword)
    private String serverScriptPath;

    @Field(type = FieldType.Keyword)
    private String wiki;

    @Data
    public static class Length {
        @Field(type = FieldType.Integer)
        private Integer old;

        @JsonProperty("new")
        @Field(type = FieldType.Integer)
        private Integer newLength;
    }

    @Data
    public static class Revision {
        @Field(type = FieldType.Long)
        private Long old;

        @JsonProperty("new")
        @Field(type = FieldType.Long)
        private Long newRevision;
    }
}
