package projeto.api.dtos.resources;

import lombok.Getter;
import lombok.Setter;
import projeto.api.dtos.DurationDTO;

@Getter @Setter
public class ResourceActivityPerformanceDTO
{
    private String resource;
    private long meanMillis;
    private DurationDTO mean;

    public ResourceActivityPerformanceDTO(String resource, long meanMillis) {
        this.resource = resource;
        this.meanMillis = meanMillis;
        this.mean = new DurationDTO( meanMillis );
    }

}
