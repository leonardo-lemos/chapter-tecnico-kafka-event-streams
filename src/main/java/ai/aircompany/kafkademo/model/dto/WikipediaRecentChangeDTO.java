package ai.aircompany.kafkademo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikipediaRecentChangeDTO {

    @JsonProperty("$schema")
    private String schema;

    private Long id;

    private String type;

    private Integer namespace;

    private String title;

    @JsonProperty("title_url")
    private String titleUrl;

    private String comment;

    private String parsedcomment;

    private Long timestamp;

    private String user;

    private Boolean bot;

    @JsonProperty("notify_url")
    private String notifyUrl;

    private Boolean minor;

    private Boolean patrolled;

    private LengthDto length;

    private RevisionDto revision;

    @JsonProperty("server_url")
    private String serverUrl;

    @JsonProperty("server_name")
    private String serverName;

    @JsonProperty("server_script_path")
    private String serverScriptPath;

    private String wiki;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LengthDto {

        private Integer old;

        @JsonProperty("new")
        private Integer newLength;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RevisionDto {
        private Long old;

        @JsonProperty("new")
        private Long newRevision;
    }
}
